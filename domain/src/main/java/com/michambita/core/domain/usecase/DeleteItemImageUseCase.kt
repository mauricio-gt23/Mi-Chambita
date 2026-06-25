package com.michambita.domain.usecase

import com.michambita.domain.repository.ItemImageRepository
import javax.inject.Inject

class DeleteItemImageUseCase @Inject constructor(
    private val itemImageRepository: ItemImageRepository
) {
    suspend operator fun invoke(url: String): Result<Unit> {
        return itemImageRepository.deleteItemImage(url)
    }
}
