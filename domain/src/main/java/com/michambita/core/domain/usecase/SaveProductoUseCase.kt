package com.michambita.domain.usecase

import com.michambita.domain.model.Producto
import com.michambita.domain.repository.AuthRepository
import com.michambita.domain.repository.ProductoRepository
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class SaveProductoUseCase @Inject constructor(
    private val productoRepository: ProductoRepository,
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(producto: Producto): Result<Unit> {
        authRepository.getCurrentUser().firstOrNull()?.let { userId ->
            producto.userId = userId
        }
        return productoRepository.saveProducto(producto)
    }
}
