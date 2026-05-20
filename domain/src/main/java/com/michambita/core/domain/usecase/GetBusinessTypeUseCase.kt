package com.michambita.domain.usecase

import com.michambita.domain.enums.BusinessType
import com.michambita.domain.repository.preference.BusinessTypePreferencesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetBusinessTypeUseCase @Inject constructor(
    private val businessTypeRepository: BusinessTypePreferencesRepository
) {
    operator fun invoke(): Flow<BusinessType?> =
        businessTypeRepository.businessTypeFlow
}
