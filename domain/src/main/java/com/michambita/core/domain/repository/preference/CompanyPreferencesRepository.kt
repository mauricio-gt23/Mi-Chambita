package com.michambita.domain.repository.preference

import com.michambita.domain.model.Company
import kotlinx.coroutines.flow.Flow

interface CompanyPreferencesRepository {
    val companyFlow: Flow<Company?>
    suspend fun saveCompany(company: Company)
    suspend fun clearCompany()
}
