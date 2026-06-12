package com.michambita.domain.usecase

import com.michambita.domain.model.ProfileData
import com.michambita.domain.repository.EmpresaRepository
import javax.inject.Inject

class GetProfileUseCase @Inject constructor(
    private val loadUserUseCase: LoadUserUseCase,
    private val empresaRepository: EmpresaRepository,
) {
    suspend operator fun invoke(): Result<ProfileData> {
        val userResult = loadUserUseCase()
        return userResult.fold(
            onSuccess = { user ->
                val empresaResult = user.idEmpresa?.let { empresaRepository.getEmpresaById(it) }
                val empresa = empresaResult?.getOrNull()
                Result.success(ProfileData(user = user, empresa = empresa))
            },
            onFailure = { Result.failure(it) }
        )
    }
}
