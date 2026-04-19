package com.michambita.core.domain.usecase

import com.michambita.core.domain.motor.BusinessMotor
import com.michambita.core.domain.repository.preference.BusinessTypePreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetCurrentMotorUseCase @Inject constructor(
    private val businessTypeRepository: BusinessTypePreferencesRepository
) {
    operator fun invoke(): Flow<BusinessMotor?> =
        businessTypeRepository.businessTypeFlow.map { type ->
            type?.let { BusinessMotor.fromType(it) }
        }
}
