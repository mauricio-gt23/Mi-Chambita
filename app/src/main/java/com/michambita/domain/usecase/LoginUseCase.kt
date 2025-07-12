package com.michambita.domain.usecase

import com.michambita.data.repository.AuthRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend fun loginWithEmail(email: String): Result<String> {
        return authRepository.loginWithEmailLink(email)
    }

    suspend fun loginWithPhone(phone: String): Result<Unit> {
        return authRepository.loginWithPhoneNumber(phone)
    }
}