package com.michambita.core.domain.usecase

import com.michambita.core.domain.repository.MovimientoRepository
import javax.inject.Inject

class SyncMovimientosUseCase @Inject constructor(
    private val movimientoRepository: MovimientoRepository
) {
    suspend operator fun invoke(): Result<Unit> {
        return movimientoRepository.syncPendingMovimientosToFirebase()
    }
}
