package com.michambita.domain.usecase

import com.michambita.data.repository.ProductoRepository
import com.michambita.domain.model.Producto
import javax.inject.Inject

class GetProductoUseCase @Inject constructor(
    private val productoRepository: ProductoRepository
) {
    suspend operator fun invoke(id: String): Result<Producto> {
        return productoRepository.getProducto(id)
    }
}