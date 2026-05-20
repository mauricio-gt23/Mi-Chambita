package com.michambita.core.domain.usecase

import com.michambita.domain.repository.AuthRepository
import com.michambita.domain.repository.EmpresaRepository
import com.michambita.domain.repository.UserRepository
import com.michambita.domain.repository.preference.BusinessTypePreferencesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val empresaRepository: EmpresaRepository,
    private val businessTypePreferencesRepository: BusinessTypePreferencesRepository
) {
    fun getCurrentUser(): Flow<String?> = authRepository.getCurrentUser()

    suspend operator fun invoke(email: String, password: String): Result<String> {
        // 1. Autenticar con Firebase
        val loginResult = authRepository.login(email, password)
        if (loginResult.isFailure) return loginResult
        val uid = loginResult.getOrNull()!!

        // 2. Obtener usuario → idEmpresa
        val user = userRepository.getUser(uid).getOrNull()
            ?: return Result.failure(Exception("No se pudo obtener el perfil de usuario"))
        val idEmpresa = user.idEmpresa
            ?: return Result.failure(Exception("El usuario no tiene empresa asociada"))

        // 3. Obtener empresa → businessType
        val empresa = empresaRepository.getEmpresaById(idEmpresa).getOrNull()
            ?: return Result.failure(Exception("La empresa asociada no existe"))
        val businessType = empresa.businessType
            ?: return Result.failure(Exception("La empresa no tiene tipo de negocio configurado"))

        // 4. Persistir BusinessType en DataStore
        businessTypePreferencesRepository.saveBusinessType(businessType)

        return Result.success(uid)
    }
}