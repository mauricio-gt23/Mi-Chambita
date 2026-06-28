package com.michambita.data.local.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.michambita.domain.model.User
import com.michambita.domain.repository.preference.UserPreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserPreferencesRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : UserPreferencesRepository {

    private object PreferencesKeys {
        val USER_UID = stringPreferencesKey("user_uid")
        val USER_NAME = stringPreferencesKey("user_name")
        val USER_EMAIL = stringPreferencesKey("user_email")
        val USER_COMPANY_ID = stringPreferencesKey("user_company_id")
        val USER_CTRL_ADMIN = booleanPreferencesKey("user_ctrl_admin")
    }

    override val userFlow: Flow<User?> = dataStore.data
        .map { preferences ->
            val userId = preferences[PreferencesKeys.USER_UID] ?: return@map null
            User(
                userId = userId,
                name = preferences[PreferencesKeys.USER_NAME],
                email = preferences[PreferencesKeys.USER_EMAIL],
                companyId = preferences[PreferencesKeys.USER_COMPANY_ID],
                ctrlAdmin = preferences[PreferencesKeys.USER_CTRL_ADMIN] ?: false
            )
        }

    override suspend fun saveUser(user: User) {
        dataStore.edit { preferences ->
            user.userId?.let { preferences[PreferencesKeys.USER_UID] = it }
            user.name?.let { preferences[PreferencesKeys.USER_NAME] = it }
            user.email?.let { preferences[PreferencesKeys.USER_EMAIL] = it }
            user.companyId?.let { preferences[PreferencesKeys.USER_COMPANY_ID] = it }
            preferences[PreferencesKeys.USER_CTRL_ADMIN] = user.ctrlAdmin
        }
    }

    override suspend fun clearUser() {
        dataStore.edit { preferences ->
            preferences.remove(PreferencesKeys.USER_UID)
            preferences.remove(PreferencesKeys.USER_NAME)
            preferences.remove(PreferencesKeys.USER_EMAIL)
            preferences.remove(PreferencesKeys.USER_COMPANY_ID)
            preferences.remove(PreferencesKeys.USER_CTRL_ADMIN)
        }
    }
}
