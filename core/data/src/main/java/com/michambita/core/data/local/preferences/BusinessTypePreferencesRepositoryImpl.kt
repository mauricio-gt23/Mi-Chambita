package com.michambita.core.data.local.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.michambita.core.domain.enums.BusinessType
import com.michambita.core.domain.repository.preference.BusinessTypePreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class BusinessTypePreferencesRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>,
): BusinessTypePreferencesRepository {

    private object PreferencesKeys {
        val BUSINESS_TYPE = stringPreferencesKey("business_type")
    }

    override val businessTypeFlow: Flow<BusinessType?> = dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.BUSINESS_TYPE]?.let {
                try {
                    BusinessType.valueOf(it)
                } catch (_: IllegalArgumentException) {
                    null
                }
            }
        }

    override suspend fun saveBusinessType(type: BusinessType) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.BUSINESS_TYPE] = type.name
        }
    }
}