package com.michambita.data.repository

interface AuthRepository {
    suspend fun login(email: String, password: String): Result<String>
    suspend fun register(nombre: String, email: String, password: String): Result<String>
}