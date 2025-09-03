package com.michambita.domain.usecase

import com.michambita.data.repository.AuthRepository
import com.michambita.data.repository.ProductoRepository
import com.michambita.domain.model.Producto
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