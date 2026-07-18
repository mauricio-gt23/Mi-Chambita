package com.michambita.domain.usecase

import com.michambita.domain.model.User
import com.michambita.domain.repository.preference.UserPreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LoadUserUseCase @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository,
) {
    fun getCurrentUserId(): Flow<String?> = userPreferencesRepository.userFlow.map { it?.userId }

    suspend operator fun invoke(): Result<User> {
        val user = userPreferencesRepository.userFlow.firstOrNull()
            ?: return Result.failure(Exception("Usuario no autenticado"))
        return Result.success(user)
    }
}
