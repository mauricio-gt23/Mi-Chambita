package com.michambita.core.domain.usecase

import com.michambita.core.domain.model.Movimiento
import com.michambita.core.domain.repository.SynchronizationRepository
import javax.inject.Inject

class UpdateMovimientoUseCase @Inject constructor(
    private val movimientoRepository: SynchronizationRepository,
) {
    suspend operator fun invoke(movimiento: Movimiento): Result<Unit> {
        val movimientoToUpdate = movimiento.copy(sincronizado = false)
        return movimientoRepository.updateMovimiento(movimientoToUpdate)
    }
}
