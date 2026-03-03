package com.michambita.core.domain.usecase

import com.michambita.core.domain.model.Movimiento
import com.michambita.core.domain.repository.SynchronizationRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllMovimientoUseCase @Inject constructor(
    private val movimientoRepository: SynchronizationRepository
) {
    operator fun invoke(): Flow<List<Movimiento>> = movimientoRepository.getAllMovimientos()
}
