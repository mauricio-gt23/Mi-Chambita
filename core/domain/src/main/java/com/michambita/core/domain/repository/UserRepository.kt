package com.michambita.core.domain.repository

import com.michambita.core.domain.model.User

interface UserRepository {
    suspend fun getUser(userId: String): Result<User>
}
