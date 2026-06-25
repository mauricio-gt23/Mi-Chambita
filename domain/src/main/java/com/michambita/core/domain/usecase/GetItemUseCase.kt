package com.michambita.domain.usecase

import com.michambita.domain.model.Item
import com.michambita.domain.repository.ItemRepository
import javax.inject.Inject

class GetItemUseCase @Inject constructor(
    private val itemRepository: ItemRepository
) {
    suspend operator fun invoke(id: String): Result<Item> {
        return itemRepository.getItem(id)
    }
}
