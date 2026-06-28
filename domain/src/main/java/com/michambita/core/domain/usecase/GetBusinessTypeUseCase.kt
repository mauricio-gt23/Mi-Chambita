package com.michambita.domain.usecase

import com.michambita.domain.enums.BusinessType
import com.michambita.domain.repository.preference.CompanyPreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetBusinessTypeUseCase @Inject constructor(
    private val companyPreferencesRepository: CompanyPreferencesRepository
) {
    operator fun invoke(): Flow<BusinessType?> =
        companyPreferencesRepository.companyFlow.map { it?.businessType }
}
