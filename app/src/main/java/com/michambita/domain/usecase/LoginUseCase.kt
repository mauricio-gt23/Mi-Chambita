package com.michambita.domain.usecase

import com.michambita.data.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    fun getCurrentUser(): Flow<String?> = authRepository.getCurrentUser()

    suspend operator fun invoke(email: String, password: String): Result<String> {
        return authRepository.login(email, password)
    }

}