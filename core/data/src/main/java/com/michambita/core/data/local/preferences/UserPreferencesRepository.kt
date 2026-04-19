package com.michambita.core.data.local.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.michambita.core.domain.enums.BusinessType
import com.michambita.core.domain.motor.BusinessTypeProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserPreferencesRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : BusinessTypeProvider {

    private object PreferencesKeys {
        val USER_UID = stringPreferencesKey("user_uid")
        val BUSINESS_TYPE = stringPreferencesKey("business_type")
    }

    val userUidFlow: Flow<String?> = dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.USER_UID]
        }

    override val businessTypeFlow: Flow<BusinessType?> = dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.BUSINESS_TYPE]?.let {
                try { BusinessType.valueOf(it) } catch (_: IllegalArgumentException) { null }
            }
        }

    suspend fun saveUserUid(uid: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.USER_UID] = uid
        }
    }

    override suspend fun saveBusinessType(type: BusinessType) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.BUSINESS_TYPE] = type.name
        }
    }

    suspend fun clearUserUid() {
        dataStore.edit { preferences ->
            preferences.remove(PreferencesKeys.USER_UID)
        }
    }
}

