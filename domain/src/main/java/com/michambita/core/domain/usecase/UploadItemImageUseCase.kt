package com.michambita.domain.usecase

import android.net.Uri
import com.michambita.domain.repository.ItemImageRepository
import javax.inject.Inject

class UploadItemImageUseCase @Inject constructor(
    private val itemImageRepository: ItemImageRepository
) {
    suspend operator fun invoke(uri: Uri): Result<String> {
        return itemImageRepository.uploadItemImage(uri)
    }
}
