package com.michambita.core.domain.repository

import com.michambita.core.domain.model.Producto

interface ProductoRepository {
    suspend fun saveProducto(producto: Producto): Result<Unit>
    suspend fun getProducto(id: String): Result<Producto>
    suspend fun deleteProducto(id: String): Result<Unit>
    suspend fun getAllProductosByUserId(userId: String): Result<List<Producto>>
    suspend fun updateProductoStock(id: String, stock: Int): Result<Unit>
}
