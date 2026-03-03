package com.michambita.core.data.repository.impl

import com.google.firebase.firestore.FirebaseFirestore
import com.michambita.core.data.database.dao.SynchronizationDAO
import com.michambita.core.data.database.entity.toDomain
import com.michambita.core.data.model.toModel
import com.michambita.core.domain.model.Movimiento
import com.michambita.core.domain.repository.MovimientoRepository
import com.michambita.core.domain.repository.SynchronizationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class MovimientoRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val synchronizationRepository: SynchronizationRepository
) : MovimientoRepository {

    private val movimientoCollection = firestore.collection("movimientos")

    override suspend fun saveMovimientoLocally(movimiento: Movimiento): Result<Unit> {
        return synchronizationRepository.addMovimiento(movimiento)
    }

    override suspend fun syncPendingMovimientosToFirebase(): Result<Unit> {
        return try {
            val pendingMovimientos = synchronizationRepository
                .getAllMovimientoPendientes()
                .firstOrNull() ?: emptyList()

            if (pendingMovimientos.isEmpty()) return Result.success(Unit)

            val syncedIds = mutableListOf<Long>()

            pendingMovimientos.chunked(500).forEach { chunk ->
                firestore.runBatch { batch ->
                    chunk.forEach { m ->
                        val docRef = movimientoCollection.document(m.id.toString())
                        batch.set(docRef, m.toModel())
                        syncedIds.add(m.id!!)
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
}
