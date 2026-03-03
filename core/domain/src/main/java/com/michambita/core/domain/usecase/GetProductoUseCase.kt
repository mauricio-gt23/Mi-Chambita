package com.michambita.core.domain.usecase

import com.michambita.core.domain.model.Producto
import com.michambita.core.domain.repository.ProductoRepository
import javax.inject.Inject

class GetProductoUseCase @Inject constructor(
    private val productoRepository: ProductoRepository
) {
    suspend operator fun invoke(id: String): Result<Producto> {
        return productoRepository.getProducto(id)
    }
}
