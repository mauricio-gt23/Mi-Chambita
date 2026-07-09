package com.michambita.domain.usecase

import com.michambita.domain.repository.ItemRepository
import javax.inject.Inject

class UpdateItemStockUseCase @Inject constructor(
    private val itemRepository: ItemRepository
) {
    suspend operator fun invoke(id: String, stock: Int): Result<Unit> {
        return itemRepository.updateItemStock(id, stock)
    }
}
