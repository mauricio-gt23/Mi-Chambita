package com.michambita.domain.usecase

import com.michambita.data.repository.AuthRepository
import com.michambita.data.repository.EmpresaRepository
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val empresaRepository: EmpresaRepository
) {

    suspend operator fun invoke(name: String, email: String, password: String): Result<String> {
        return authRepository.register(name, email, password)
    }
}