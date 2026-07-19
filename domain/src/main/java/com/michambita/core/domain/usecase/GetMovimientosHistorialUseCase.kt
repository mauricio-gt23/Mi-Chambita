package com.michambita.domain.usecase

import com.michambita.domain.model.Movimiento
import com.michambita.domain.repository.MovimientoRepository
import com.michambita.domain.repository.preference.CompanyPreferencesRepository
import kotlinx.coroutines.flow.firstOrNull
import java.util.Date
import javax.inject.Inject

class GetMovimientosHistorialUseCase @Inject constructor(
    private val movimientoRepository: MovimientoRepository,
    private val companyPreferencesRepository: CompanyPreferencesRepository,
) {
    suspend operator fun invoke(
        fechaInicio: Date,
        fechaFin: Date
    ): Result<List<Movimiento>> {
        val company = companyPreferencesRepository.companyFlow.firstOrNull()
            ?: return Result.failure(Exception("No company found"))
        val companyId = company.id
            ?: return Result.failure(Exception("Company has no ID"))
        return movimientoRepository.getMovimientosPorPeriodo(
            companyId = companyId,
            fechaInicio = fechaInicio,
            fechaFin = fechaFin
        )
    }
}
