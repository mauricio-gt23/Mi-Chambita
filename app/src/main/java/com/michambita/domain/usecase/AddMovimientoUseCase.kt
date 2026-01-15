package com.michambita.domain.usecase

import com.michambita.data.repository.AuthRepository
import com.michambita.data.repository.MovimientoRepository
import com.michambita.domain.model.Movimiento
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class AddMovimientoUseCase @Inject constructor(
    private val movimientoRepository: MovimientoRepository,
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(movimiento: Movimiento): Result<Unit> {
        val userId = authRepository.getCurrentUser().firstOrNull()
        movimiento.userId = userId
        return movimientoRepository.saveMovimientoLocally(movimiento)
    }
}