package com.michambita.data.repository

import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun login(email: String, password: String): Result<String>
    suspend fun register(
        name: String, 
        email: String, 
        password: String,
        idEmpresa: String,
        ctrlAdmin: Boolean
    ): Result<String>
    suspend fun checkEmailExists(email: String): Result<Boolean>
    suspend fun logout()
    fun getCurrentUser(): Flow<String?>
}