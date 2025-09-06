package com.michambita.domain.usecase

import com.michambita.data.repository.AuthRepository
import com.michambita.data.repository.SynchronizationRepository
import com.michambita.domain.model.Movimiento
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class AddMovimientoUseCase @Inject constructor(
    private val movimientoRepository: SynchronizationRepository,
    private val authRepository: AuthRepository,
) {
    operator fun invoke(): Flow<List<Movimiento>> = movimientoRepository.getAllMovimientoPendientes()

    suspend operator fun invoke(movimiento: Movimiento): Result<Unit> {
        val userId = authRepository.getCurrentUser().firstOrNull()
        movimiento.userId = userId
        return movimientoRepository.addMovimiento(movimiento)
    }
}