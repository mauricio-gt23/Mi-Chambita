package com.michambita.domain.usecase

import com.michambita.data.repository.MovimientoRepository
import javax.inject.Inject

class SyncMovimientosUseCase @Inject constructor(
    private val movimientoRepository: MovimientoRepository
) {
    suspend operator fun invoke(): Result<Unit> {
        return movimientoRepository.syncPendingMovimientosToFirebase()
    }
}