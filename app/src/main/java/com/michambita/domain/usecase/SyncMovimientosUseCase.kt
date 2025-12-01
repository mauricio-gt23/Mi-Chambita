package com.michambita.domain.usecase

import com.michambita.data.repository.MovimientoRepository
import com.michambita.data.repository.SynchronizationRepository
import com.michambita.domain.model.Movimiento
import javax.inject.Inject

class SyncMovimientosUseCase @Inject constructor(
    private val synchronizationRepository: SynchronizationRepository,
    private val movimientoRepository: MovimientoRepository
) {
    suspend fun invoke(movimientos: List<Movimiento>): Result<Unit> {
        movimientoRepository.sincronizarMovimientos(movimientos)
        synchronizationRepository.deleteAllMovimientoPendientes()
        return Result.success(Unit)
    }
}