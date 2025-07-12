package com.michambita.data.repository

interface AuthRepository {
    suspend fun loginWithEmailLink(email: String): Result<String>
    suspend fun loginWithPhoneNumber(phone: String): Result<Unit>
}