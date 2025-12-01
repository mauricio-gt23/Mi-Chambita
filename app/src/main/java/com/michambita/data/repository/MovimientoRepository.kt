package com.michambita.data.repository

import com.michambita.domain.model.Movimiento

interface MovimientoRepository {
    suspend fun sincronizarMovimientos(movimientos: List<Movimiento>): Result<Unit>
}