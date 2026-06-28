package com.michambita.domain.usecase

import com.michambita.domain.model.Movimiento
import com.michambita.domain.repository.SynchronizationRepository
import com.michambita.domain.repository.preference.CompanyPreferencesRepository
import com.michambita.domain.repository.preference.UserPreferencesRepository
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class AddMovimientoUseCase @Inject constructor(
    private val movimientoRepository: SynchronizationRepository,
    private val userPreferencesRepository: UserPreferencesRepository,
    private val companyPreferencesRepository: CompanyPreferencesRepository,
) {
    suspend operator fun invoke(movimiento: Movimiento): Result<Unit> {
        val user = userPreferencesRepository.userFlow.firstOrNull()
            ?: return Result.failure(Exception("Usuario no autenticado"))
        val company = companyPreferencesRepository.companyFlow.firstOrNull()
            ?: return Result.failure(Exception("No hay empresa en sesión"))
        movimiento.companyId = company.id
        return movimientoRepository.addMovimiento(
            movimiento.copy(createdByUserId = user.userId)
        )
    }
}
