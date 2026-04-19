package com.michambita.core.domain.motor

import com.michambita.core.domain.enums.BusinessType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Resolves the current BusinessMotor from stored user preferences.
 * Returns null if no BusinessType has been set yet.
 */
interface BusinessTypeProvider {
    val businessTypeFlow: Flow<BusinessType?>
    suspend fun saveBusinessType(type: BusinessType)
}

class GetCurrentMotorUseCase(
    private val businessTypeProvider: BusinessTypeProvider
) {
    operator fun invoke(): Flow<BusinessMotor?> =
        businessTypeProvider.businessTypeFlow.map { type ->
            type?.let { BusinessMotor.fromType(it) }
        }
}
