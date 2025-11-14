package com.michambita.domain.usecase

import com.michambita.data.repository.ProductoRepository
import javax.inject.Inject

class UpdateProductoStockUseCase @Inject constructor(
    private val productoRepository: ProductoRepository
) {
    suspend operator fun invoke(id: String, stock: Int): Result<Unit> {
        return productoRepository.updateProductoStock(id, stock)
    }
}