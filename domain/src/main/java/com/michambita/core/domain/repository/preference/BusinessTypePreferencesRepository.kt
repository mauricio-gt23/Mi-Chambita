package com.michambita.domain.repository.preference

import com.michambita.domain.enums.BusinessType
import kotlinx.coroutines.flow.Flow

interface BusinessTypePreferencesRepository {
    val businessTypeFlow: Flow<BusinessType?>
    suspend fun saveBusinessType(type: BusinessType)
    suspend fun clearBusinessType()
}