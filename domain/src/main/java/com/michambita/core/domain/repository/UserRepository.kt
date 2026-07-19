package com.michambita.domain.repository

import com.michambita.domain.model.User

interface UserRepository {
    suspend fun saveUserProfile(
        userId: String,
        name: String,
        email: String,
        companyId: String,
        ctrlAdmin: Boolean
    ): Result<Unit>
    suspend fun fetchUser(userId: String): Result<User>
}
