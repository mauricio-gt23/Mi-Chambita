package com.michambita.core.domain.usecase

import com.michambita.core.domain.repository.ProductoRepository
import javax.inject.Inject

class UpdateProductoStockUseCase @Inject constructor(
    private val productoRepository: ProductoRepository
) {
    suspend operator fun invoke(id: String, stock: Int): Result<Unit> {
        return productoRepository.updateProductoStock(id, stock)
    }
}
