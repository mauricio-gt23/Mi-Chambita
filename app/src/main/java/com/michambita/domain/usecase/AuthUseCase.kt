package com.michambita.domain.usecase

import com.michambita.data.repository.AuthRepository
import javax.inject.Inject

class AuthUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend fun login(email: String, password: String): Result<String> {
        return authRepository.login(email, password)
    }

    suspend fun register(name: String, email: String, password: String): Result<String> {
        return authRepository.register(name, email, password)
    }
}