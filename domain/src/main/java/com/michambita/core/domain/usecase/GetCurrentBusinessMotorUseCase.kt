package com.michambita.domain.usecase

import com.michambita.domain.motor.BusinessMotor
import com.michambita.domain.repository.preference.BusinessTypePreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetCurrentBusinessMotorUseCase @Inject constructor(
    private val businessTypeRepository: BusinessTypePreferencesRepository
) {
    operator fun invoke(): Flow<BusinessMotor?> =
        businessTypeRepository.businessTypeFlow.map { type ->
            type?.let { BusinessMotor.fromType(it) }
        }
}
