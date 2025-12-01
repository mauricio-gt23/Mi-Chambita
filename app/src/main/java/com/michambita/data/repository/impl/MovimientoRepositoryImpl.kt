package com.michambita.data.repository.impl

import com.google.firebase.firestore.FirebaseFirestore
import com.michambita.data.model.toModel
import com.michambita.data.repository.MovimientoRepository
import com.michambita.domain.model.Movimiento
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class MovimientoRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : MovimientoRepository {
    private val movimientoCollection = firestore.collection("movimientos")

    override suspend fun sincronizarMovimientos(movimientos: List<Movimiento>): Result<Unit> {
        return try {
            movimientos.chunked(500).forEach { chunk ->
                firestore.runBatch { batch ->
                    chunk.forEach { m ->
                        val docRef = movimientoCollection.document(m.id.toString())
                        batch.set(docRef, m.toModel())
                    }
                }.await()
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}
