package com.michambita.domain.usecase

import com.michambita.domain.model.Company
import com.michambita.domain.model.ProfileData
import com.michambita.domain.model.User
import com.michambita.domain.repository.preference.CompanyPreferencesRepository
import com.michambita.domain.repository.preference.UserPreferencesRepository
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class GetProfileUseCase @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val companyPreferencesRepository: CompanyPreferencesRepository,
) {
    suspend operator fun invoke(): Result<ProfileData> {
        val user: User = userPreferencesRepository.userFlow.firstOrNull()
            ?: return Result.failure(Exception("Usuario no autenticado"))
        val company: Company? = companyPreferencesRepository.companyFlow.firstOrNull()
        return Result.success(ProfileData(user = user, company = company))
    }
}
