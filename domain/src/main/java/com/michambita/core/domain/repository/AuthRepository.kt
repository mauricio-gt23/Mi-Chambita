package com.michambita.domain.repository

interface AuthRepository {
    suspend fun login(email: String, password: String): Result<String>
    suspend fun createAuthAccount(email: String, password: String): Result<String>
    suspend fun deleteAuthAccount(): Result<Unit>
    suspend fun sendPasswordResetEmail(email: String): Result<Unit>
    suspend fun logout()
}
