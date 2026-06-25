package com.michambita.domain.usecase

import com.michambita.domain.model.Item
import com.michambita.domain.repository.AuthRepository
import com.michambita.domain.repository.ItemRepository
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class LoadAllItemsByUserIdUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val itemRepository: ItemRepository
) {
    suspend fun invoke(): Result<List<Item>> {
        val userId = authRepository.getCurrentUser().firstOrNull()
        return itemRepository.getAllItemsByUserId(userId!!)
    }
}
