package com.michambita.data.repository.impl

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.michambita.core.data.util.Constant
import com.michambita.data.database.dao.SynchronizationDAO
import com.michambita.data.database.entity.toDomain
import com.michambita.data.model.MovimientoModel
import com.michambita.data.model.toDomain
import com.michambita.data.model.toModel
import com.michambita.domain.model.Movimiento
import com.michambita.domain.repository.MovimientoRepository
import com.michambita.domain.repository.SynchronizationRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

class MovimientoRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val synchronizationRepository: SynchronizationRepository
) : MovimientoRepository {

    // ── Offline-first methods (preserved for future use) ──────────────

    private val movimientoCollection = firestore.collection(Constant.Documents.MOVIMIENTOS)

    override suspend fun saveMovimientoLocally(movimiento: Movimiento): Result<Unit> {
        return synchronizationRepository.addMovimiento(movimiento)
    }

    override suspend fun syncPendingMovimientosToFirebase(): Result<Unit> {
        return try {
            val pendingMovimientos = synchronizationRepository
                .getAllMovimientoPendientes()
                .firstOrNull() ?: emptyList()

            if (pendingMovimientos.isEmpty()) return Result.success(Unit)

            val syncedIds = mutableListOf<String>()

            pendingMovimientos.chunked(500).forEach { chunk ->
                firestore.runBatch { batch ->
                    chunk.forEach { m ->
                        val docRef = movimientoCollection.document(m.id.toString())
                        batch.set(docRef, m.toModel())
                        m.id?.let { syncedIds.add(it) }
                    }
                }.await()
            }

            synchronizationRepository.markMultipleAsSynchronized(syncedIds)
            synchronizationRepository.cleanOldSynchronizedMovimientos(7)

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // ── Online-first methods ──────────────────────────────────────────

    private fun movimientosCollection(companyId: String) =
        firestore.collection(Constant.Documents.EMPRESAS).document(companyId).collection(Constant.Documents.MOVIMIENTOS)

    override suspend fun addMovimientoOnline(movimiento: Movimiento, companyId: String): Result<Unit> {
        return try {
            val docRef = movimientosCollection(companyId).document()
            val movimientoWithId = movimiento.copy(id = docRef.id)
            docRef.set(movimientoWithId.toModel()).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateMovimientoOnline(movimiento: Movimiento, companyId: String): Result<Unit> {
        return try {
            val id = movimiento.id ?: return Result.failure(Exception("Movimiento sin ID"))
            movimientosCollection(companyId).document(id).set(movimiento.toModel()).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteMovimientoOnline(movimiento: Movimiento, companyId: String): Result<Unit> {
        return try {
            val id = movimiento.id ?: return Result.failure(Exception("Movimiento sin ID"))
            movimientosCollection(companyId).document(id).delete().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getMovimientosOnline(companyId: String): Flow<List<Movimiento>> = callbackFlow {
        val todayStart = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.time

        val query = movimientosCollection(companyId)
            .whereGreaterThanOrEqualTo("fechaRegistro", todayStart)
            .orderBy("fechaRegistro", Query.Direction.DESCENDING)

        val listener = query.addSnapshotListener { snapshot, error ->
            if (error != null) {
                trySend(emptyList())
                return@addSnapshotListener
            }

            val movimientos = snapshot?.documents?.mapNotNull { doc ->
                doc.toObject(MovimientoModel::class.java)?.copy(id = doc.id)?.toDomain()
            } ?: emptyList()

            trySend(movimientos)
        }

        awaitClose { listener.remove() }
    }

    // ── History query (on-demand, paginated) ──────────────────────────

    override suspend fun getMovimientosHistorial(
        companyId: String,
        fechaInicio: Date,
        fechaFin: Date,
        limit: Int,
        lastDocumentId: String?
    ): Result<List<Movimiento>> {
        return try {
            var query = movimientosCollection(companyId)
                .whereGreaterThanOrEqualTo("fechaRegistro", fechaInicio)
                .whereLessThan("fechaRegistro", fechaFin)
                .orderBy("fechaRegistro", Query.Direction.DESCENDING)
                .limit(limit.toLong())

            // Pagination: fetch the last document snapshot and use startAfter
            if (lastDocumentId != null) {
                val lastDoc = movimientosCollection(companyId)
                    .document(lastDocumentId).get().await()
                if (lastDoc.exists()) {
                    query = query.startAfter(lastDoc)
                }
            }

            val snapshot = query.get().await()
            val movimientos = snapshot.documents.mapNotNull { doc ->
                doc.toObject(MovimientoModel::class.java)
                    ?.copy(id = doc.id)?.toDomain()
            }
            Result.success(movimientos)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
