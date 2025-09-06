package com.michambita.data.repository

import com.michambita.domain.model.Movimiento
import kotlinx.coroutines.flow.Flow

interface SynchronizationRepository {
    fun getAllMovimientoPendientes() : Flow<List<Movimiento>>

    suspend fun addMovimiento(movimiento: Movimiento): Result<Unit>

    suspend fun updateMovimiento(movimiento: Movimiento): Result<Unit>

    suspend fun deleteMovimiento(movimiento: Movimiento): Result<Unit>
}