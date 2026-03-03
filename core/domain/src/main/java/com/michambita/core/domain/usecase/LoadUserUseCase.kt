package com.michambita.core.domain.usecase

import com.michambita.core.domain.model.User
import com.michambita.core.domain.repository.AuthRepository
import com.michambita.core.domain.repository.UserRepository
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class LoadUserUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(): Result<User> {
        val userId = authRepository.getCurrentUser().firstOrNull()
        return userRepository.getUser(userId!!)
    }
}
