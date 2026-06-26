package com.michambita.domain.usecase

import com.michambita.domain.model.Item
import com.michambita.domain.repository.AuthRepository
import com.michambita.domain.repository.ItemRepository
import com.michambita.domain.repository.UserRepository
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class SaveItemUseCase @Inject constructor(
    private val itemRepository: ItemRepository,
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
) {
    suspend operator fun invoke(item: Item): Result<Unit> {
        val userId = authRepository.getCurrentUser().firstOrNull()
            ?: return Result.failure(Exception("Usuario no autenticado"))
        val user = userRepository.getUser(userId).getOrNull()
            ?: return Result.failure(Exception("No se pudo obtener el perfil de usuario"))
        item.companyId = user.companyId
        return itemRepository.saveItem(item)
    }
}
