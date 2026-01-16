package com.michambita.domain.usecase

import com.michambita.data.repository.SynchronizationRepository
import com.michambita.domain.model.Movimiento
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllMovimientoUseCase @Inject constructor(
    private val movimientoRepository: SynchronizationRepository
) {
    operator fun invoke(): Flow<List<Movimiento>> = movimientoRepository.getAllMovimientos()
}