package com.michambita.domain.usecase

import com.michambita.domain.model.Item
import com.michambita.domain.repository.AuthRepository
import com.michambita.domain.repository.ItemRepository
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class SaveItemUseCase @Inject constructor(
    private val itemRepository: ItemRepository,
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(item: Item): Result<Unit> {
        authRepository.getCurrentUser().firstOrNull()?.let { userId ->
            item.userId = userId
        }
        return itemRepository.saveItem(item)
    }
}
