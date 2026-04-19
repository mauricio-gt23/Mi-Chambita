package com.michambita.core.domain.repository.preference

import com.michambita.core.domain.enums.BusinessType
import kotlinx.coroutines.flow.Flow

interface BusinessTypePreferencesRepository {
    val businessTypeFlow: Flow<BusinessType?>
    suspend fun saveBusinessType(type: BusinessType)
}