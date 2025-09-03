package com.michambita.domain.usecase

import com.michambita.data.repository.UserRepository
import com.michambita.domain.model.User
import javax.inject.Inject

class LoadUserUseCase @Inject constructor(
    private val userRepository: UserRepository
){
    suspend operator fun invoke(userId: String): Result<User> = userRepository.getUser(userId)
}