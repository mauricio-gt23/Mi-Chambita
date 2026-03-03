package com.michambita.core.domain.repository

import com.michambita.core.domain.model.Movimiento

interface MovimientoRepository {
    suspend fun saveMovimientoLocally(movimiento: Movimiento): Result<Unit>
    suspend fun syncPendingMovimientosToFirebase(): Result<Unit>
}
