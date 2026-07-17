package com.michambita.domain.usecase

import com.michambita.domain.repository.ItemImageRepository
import javax.inject.Inject

class UploadItemImageUseCase @Inject constructor(
    private val itemImageRepository: ItemImageRepository
) {
    suspend operator fun invoke(uriString: String): Result<String> {
        return itemImageRepository.uploadItemImage(uriString)
    }
}
