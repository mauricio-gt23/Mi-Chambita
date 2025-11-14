package com.michambita.domain.usecase

import com.michambita.data.repository.ProductoImageRepository
import javax.inject.Inject

class DeleteProductoImageUseCase @Inject constructor(
    private val productoImageRepository: ProductoImageRepository
) {
    suspend operator fun invoke(url: String): Result<Unit> {
        return productoImageRepository.deleteProductoImage(url)
    }
}