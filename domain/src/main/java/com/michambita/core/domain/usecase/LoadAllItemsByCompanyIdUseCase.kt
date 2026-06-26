package com.michambita.domain.usecase

import com.michambita.domain.model.Item
import com.michambita.domain.repository.AuthRepository
import com.michambita.domain.repository.ItemRepository
import com.michambita.domain.repository.UserRepository
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class LoadAllItemsByCompanyIdUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val itemRepository: ItemRepository
) {
    suspend fun invoke(): Result<List<Item>> {
        val userId = authRepository.getCurrentUser().firstOrNull()
            ?: return Result.failure(Exception("Usuario no autenticado"))
        val user = userRepository.getUser(userId).getOrNull()
            ?: return Result.failure(Exception("No se pudo obtener el perfil de usuario"))
        val companyId = user.companyId
            ?: return Result.failure(Exception("El usuario no tiene empresa asociada"))
        return itemRepository.getAllItemsByCompanyId(companyId)
    }
}
