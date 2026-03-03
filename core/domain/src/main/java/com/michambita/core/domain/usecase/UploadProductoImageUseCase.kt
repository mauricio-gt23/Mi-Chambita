package com.michambita.core.domain.usecase

import android.net.Uri
import com.michambita.core.domain.repository.ProductoImageRepository
import javax.inject.Inject

class UploadProductoImageUseCase @Inject constructor(
    private val productoImageRepository: ProductoImageRepository
) {
    suspend operator fun invoke(uri: Uri): Result<String> {
        return productoImageRepository.uploadProductoImage(uri)
    }
}
