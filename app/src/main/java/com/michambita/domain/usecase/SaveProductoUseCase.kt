package com.michambita.domain.usecase

import com.michambita.data.repository.ProductoRepository
import com.michambita.domain.model.Producto
import javax.inject.Inject

class SaveProductoUseCase @Inject constructor(
    private val productoRepository: ProductoRepository
){
    suspend operator fun invoke(producto: Producto): Result<Unit> = productoRepository.saveProducto(producto)
}