package com.michambita.domain.usecase

import com.michambita.data.repository.SynchronizationRepository
import com.michambita.domain.model.Movimiento
import javax.inject.Inject

class DeleteMovimientoUseCase @Inject constructor(
    private val movimientoRepository: SynchronizationRepository,
) {
    suspend operator fun invoke(movimiento: Movimiento): Result<Unit> {
        return movimientoRepository.deleteMovimiento(movimiento)
    }
}