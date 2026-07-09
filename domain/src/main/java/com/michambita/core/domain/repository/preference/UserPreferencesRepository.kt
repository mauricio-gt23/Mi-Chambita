package com.michambita.domain.repository.preference

import com.michambita.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserPreferencesRepository {
    val userFlow: Flow<User?>
    suspend fun saveUser(user: User)
    suspend fun clearUser()
}
