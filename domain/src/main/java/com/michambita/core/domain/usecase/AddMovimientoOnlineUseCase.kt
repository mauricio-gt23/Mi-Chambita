package com.michambita.domain.usecase

import com.michambita.domain.model.Movimiento
import com.michambita.domain.repository.MovimientoRepository
import com.michambita.domain.repository.preference.CompanyPreferencesRepository
import com.michambita.domain.repository.preference.UserPreferencesRepository
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class AddMovimientoOnlineUseCase @Inject constructor(
    private val movimientoRepository: MovimientoRepository,
    private val userPreferencesRepository: UserPreferencesRepository,
    private val companyPreferencesRepository: CompanyPreferencesRepository,
) {
    suspend operator fun invoke(movimiento: Movimiento): Result<Unit> {
        val user = userPreferencesRepository.userFlow.firstOrNull()
            ?: return Result.failure(Exception("Usuario no autenticado"))
        val company = companyPreferencesRepository.companyFlow.firstOrNull()
            ?: return Result.failure(Exception("No hay empresa en sesión"))
        val companyId = company.id ?: return Result.failure(Exception("El ID de la empresa no es válido"))
        val movimientoToSave = movimiento.copy(
            createdByUserId = user.userId,
            companyId = companyId
        )
        return movimientoRepository.addMovimientoOnline(movimientoToSave, companyId)
    }
}
