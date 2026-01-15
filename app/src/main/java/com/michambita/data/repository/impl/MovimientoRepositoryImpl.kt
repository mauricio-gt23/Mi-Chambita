package com.michambita.data.repository.impl

import com.google.firebase.firestore.FirebaseFirestore
import com.michambita.data.model.toModel
import com.michambita.data.repository.MovimientoRepository
import com.michambita.data.repository.SynchronizationRepository
import com.michambita.domain.model.Movimiento
import kotlinx.coroutines.flow.first
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
            // Obtener movimientos pendientes
            val pendingMovimientos = synchronizationRepository
                .getAllMovimientoPendientes()
                .first()
            
            if (pendingMovimientos.isEmpty()) {
                return Result.success(Unit)
            }

            // Sincronizar en lotes de 500
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

            // Marcar como sincronizados
            synchronizationRepository.markMultipleAsSynchronized(syncedIds)
            
            // Limpiar movimientos antiguos
            synchronizationRepository.cleanOldSynchronizedMovimientos(7)
            
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
