package com.michambita.data.repository

import com.michambita.domain.model.Movimiento
import kotlinx.coroutines.flow.Flow

interface SynchronizationRepository {
    fun getAllMovimientos(): Flow<List<Movimiento>>
    
    fun getAllMovimientoPendientes() : Flow<List<Movimiento>>

    suspend fun addMovimiento(movimiento: Movimiento): Result<Unit>

    suspend fun updateMovimiento(movimiento: Movimiento): Result<Unit>

    suspend fun deleteMovimiento(movimiento: Movimiento): Result<Unit>

    suspend fun deleteAllMovimientoPendientes(): Result<Unit>

    suspend fun markMultipleAsSynchronized(ids: List<Long>): Result<Unit>

    suspend fun cleanOldSynchronizedMovimientos(daysOld: Int = 7): Result<Unit>
}