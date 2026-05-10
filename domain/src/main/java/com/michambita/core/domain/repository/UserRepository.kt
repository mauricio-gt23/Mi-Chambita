package com.michambita.domain.repository

import com.michambita.domain.model.User

interface UserRepository {
    suspend fun getUser(userId: String): Result<User>
}
