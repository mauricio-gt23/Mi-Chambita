package com.michambita.data.repository

import com.michambita.domain.model.Movimiento
import kotlinx.coroutines.flow.Flow

interface SynchronizationRepository {
    fun getAllMovimientoPendientes() : Flow<List<Movimiento>>
}