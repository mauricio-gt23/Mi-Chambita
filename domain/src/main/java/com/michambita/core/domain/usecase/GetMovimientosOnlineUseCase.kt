package com.michambita.domain.usecase

import com.michambita.domain.model.Movimiento
import com.michambita.domain.repository.MovimientoRepository
import com.michambita.domain.repository.preference.CompanyPreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class GetMovimientosOnlineUseCase @Inject constructor(
    private val movimientoRepository: MovimientoRepository,
    private val companyPreferencesRepository: CompanyPreferencesRepository,
) {
    suspend operator fun invoke(): Flow<List<Movimiento>> {
        val company = companyPreferencesRepository.companyFlow.firstOrNull()
            ?: return emptyFlow()
        val companyId = company.id ?: return emptyFlow()
        return movimientoRepository.getMovimientosOnline(companyId)
    }
}
