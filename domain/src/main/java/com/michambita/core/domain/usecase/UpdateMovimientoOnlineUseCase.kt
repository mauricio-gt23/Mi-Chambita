package com.michambita.domain.usecase

import com.michambita.domain.model.Movimiento
import com.michambita.domain.repository.MovimientoRepository
import com.michambita.domain.repository.preference.CompanyPreferencesRepository
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class UpdateMovimientoOnlineUseCase @Inject constructor(
    private val movimientoRepository: MovimientoRepository,
    private val companyPreferencesRepository: CompanyPreferencesRepository,
) {
    suspend operator fun invoke(movimiento: Movimiento): Result<Unit> {
        val company = companyPreferencesRepository.companyFlow.firstOrNull()
            ?: return Result.failure(Exception("No hay empresa en sesión"))
        val companyId = company.id ?: return Result.failure(Exception("El ID de la empresa no es válido"))
        return movimientoRepository.updateMovimientoOnline(movimiento, companyId)
    }
}
