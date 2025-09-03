package com.michambita.domain.usecase

import com.michambita.data.repository.AuthRepository
import com.michambita.data.repository.UserRepository
import com.michambita.domain.model.User
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class LoadUserUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository,
){
    suspend operator fun invoke(): Result<User> {
        val userId = authRepository.getCurrentUser().firstOrNull()
        return userRepository.getUser(userId!!)
    }
}