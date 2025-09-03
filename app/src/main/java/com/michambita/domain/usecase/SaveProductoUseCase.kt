package com.michambita.domain.usecase

import com.michambita.data.repository.AuthRepository
import com.michambita.data.repository.ProductoRepository
import com.michambita.domain.model.Producto
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class SaveProductoUseCase @Inject constructor(
    private val productoRepository: ProductoRepository,
    private val authRepository: AuthRepository,
){
    suspend operator fun invoke(producto: Producto): Result<Unit> {
        authRepository.getCurrentUser().firstOrNull()?.let { userId ->
            producto.userId = userId
        }
        return productoRepository.saveProducto(producto)
    }
}