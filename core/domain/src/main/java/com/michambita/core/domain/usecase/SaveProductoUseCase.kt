package com.michambita.core.domain.usecase

import com.michambita.core.domain.model.Producto
import com.michambita.core.domain.repository.AuthRepository
import com.michambita.core.domain.repository.ProductoRepository
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
