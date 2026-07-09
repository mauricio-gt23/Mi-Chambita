package com.michambita.core.domain.usecase

import com.michambita.domain.model.User
import com.michambita.domain.repository.AuthRepository
import com.michambita.domain.repository.CompanyRepository
import com.michambita.domain.repository.UserRepository
import com.michambita.domain.repository.preference.CompanyPreferencesRepository
import com.michambita.domain.repository.preference.UserPreferencesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val companyRepository: CompanyRepository,
    private val userPreferencesRepository: UserPreferencesRepository,
    private val companyPreferencesRepository: CompanyPreferencesRepository
) {
    fun getCurrentUser(): Flow<String?> = authRepository.getCurrentUser()

    suspend operator fun invoke(email: String, password: String): Result<String> {
        // 1. Autenticar con Firebase
        val loginResult = authRepository.login(email, password)
        if (loginResult.isFailure) return loginResult
        val uid = loginResult.getOrNull()!!

        // 2. Obtener usuario → companyId
        val user = userRepository.getUser(uid).getOrNull()
            ?: return Result.failure(Exception("No se pudo obtener el perfil de usuario"))
        val companyId = user.companyId
            ?: return Result.failure(Exception("El usuario no tiene empresa asociada"))

        // 3. Obtener empresa
        val company = companyRepository.getCompanyById(companyId).getOrNull()
            ?: return Result.failure(Exception("La empresa asociada no existe"))

        // 4. Persistir User y Company en DataStore
        userPreferencesRepository.saveUser(user)
        companyPreferencesRepository.saveCompany(company)

        return Result.success(uid)
    }
}