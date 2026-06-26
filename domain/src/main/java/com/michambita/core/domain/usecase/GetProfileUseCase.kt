package com.michambita.domain.usecase

import com.michambita.domain.model.ProfileData
import com.michambita.domain.repository.CompanyRepository
import javax.inject.Inject

class GetProfileUseCase @Inject constructor(
    private val loadUserUseCase: LoadUserUseCase,
    private val companyRepository: CompanyRepository,
) {
    suspend operator fun invoke(): Result<ProfileData> {
        val userResult = loadUserUseCase()
        return userResult.fold(
            onSuccess = { user ->
                val companyResult = user.companyId?.let { companyRepository.getCompanyById(it) }
                val company = companyResult?.getOrNull()
                Result.success(ProfileData(user = user, company = company))
            },
            onFailure = { Result.failure(it) }
        )
    }
}
