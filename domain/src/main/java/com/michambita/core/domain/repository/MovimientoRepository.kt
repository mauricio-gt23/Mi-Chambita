package com.michambita.domain.repository

import com.michambita.domain.model.Movimiento
import kotlinx.coroutines.flow.Flow

interface MovimientoRepository {
    // Offline-first methods
    suspend fun saveMovimientoLocally(movimiento: Movimiento): Result<Unit>
    suspend fun syncPendingMovimientosToFirebase(): Result<Unit>

    // Online-first methods
    suspend fun addMovimientoOnline(movimiento: Movimiento, companyId: String): Result<Unit>
    suspend fun updateMovimientoOnline(movimiento: Movimiento, companyId: String): Result<Unit>
    suspend fun deleteMovimientoOnline(movimiento: Movimiento, companyId: String): Result<Unit>

    fun getMovimientosOnline(companyId: String): Flow<List<Movimiento>>
    suspend fun getMovimientosPorPeriodo(
        companyId: String,
        fechaInicio: java.util.Date,
        fechaFin: java.util.Date
    ): Result<List<Movimiento>>
}
