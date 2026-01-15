package com.michambita.data.repository

import com.michambita.domain.model.Movimiento

interface MovimientoRepository {
    suspend fun saveMovimientoLocally(movimiento: Movimiento): Result<Unit>
    
    suspend fun syncPendingMovimientosToFirebase(): Result<Unit>
}