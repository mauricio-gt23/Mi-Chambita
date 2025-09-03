package com.michambita.data.repository

import com.michambita.domain.model.Producto

interface ProductoRepository {
    suspend fun saveProducto(producto: Producto): Result<Unit>
    suspend fun getProducto(id: String): Result<Producto>
    suspend fun deleteProducto(id: String): Result<Unit>
    suspend fun getAllProductosByUserId(userId: String): Result<List<Producto>>
}