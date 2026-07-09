package com.michambita.data.local.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.michambita.domain.enums.BusinessType
import com.michambita.domain.model.Company
import com.michambita.domain.repository.preference.CompanyPreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CompanyPreferencesRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : CompanyPreferencesRepository {

    private object PreferencesKeys {
        val COMPANY_ID = stringPreferencesKey("company_id")
        val COMPANY_NOMBRE = stringPreferencesKey("company_nombre")
        val COMPANY_DESCRIPCION = stringPreferencesKey("company_descripcion")
        val COMPANY_BUSINESS_TYPE = stringPreferencesKey("company_business_type")
    }

    override val companyFlow: Flow<Company?> = dataStore.data
        .map { preferences ->
            val companyId = preferences[PreferencesKeys.COMPANY_ID] ?: return@map null
            Company(
                id = companyId,
                nombre = preferences[PreferencesKeys.COMPANY_NOMBRE] ?: "",
                descripcion = preferences[PreferencesKeys.COMPANY_DESCRIPCION],
                businessType = preferences[PreferencesKeys.COMPANY_BUSINESS_TYPE]?.let {
                    try {
                        BusinessType.valueOf(it)
                    } catch (_: IllegalArgumentException) {
                        null
                    }
                }
            )
        }

    override suspend fun saveCompany(company: Company) {
        dataStore.edit { preferences ->
            company.id?.let { preferences[PreferencesKeys.COMPANY_ID] = it }
            preferences[PreferencesKeys.COMPANY_NOMBRE] = company.nombre
            company.descripcion?.let { preferences[PreferencesKeys.COMPANY_DESCRIPCION] = it }
            company.businessType?.let {
                preferences[PreferencesKeys.COMPANY_BUSINESS_TYPE] = it.name
            }
        }
    }

    override suspend fun clearCompany() {
        dataStore.edit { preferences ->
            preferences.remove(PreferencesKeys.COMPANY_ID)
            preferences.remove(PreferencesKeys.COMPANY_NOMBRE)
            preferences.remove(PreferencesKeys.COMPANY_DESCRIPCION)
            preferences.remove(PreferencesKeys.COMPANY_BUSINESS_TYPE)
        }
    }
}
