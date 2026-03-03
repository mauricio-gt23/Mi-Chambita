package com.michambita.core.domain.usecase

import com.michambita.core.domain.model.Producto
import com.michambita.core.domain.repository.AuthRepository
import com.michambita.core.domain.repository.ProductoRepository
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class LoadAllProductoByUserId @Inject constructor(
    private val authRepository: AuthRepository,
    private val productoRepository: ProductoRepository
) {
    suspend fun invoke(): Result<List<Producto>> {
        val userId = authRepository.getCurrentUser().firstOrNull()
        return productoRepository.getAllProductosByUserId(userId!!)
    }
}
