package com.michambita.data.repository.impl

import com.google.firebase.firestore.FirebaseFirestore
import com.michambita.data.model.toModel
import com.michambita.data.repository.ProductoRepository
import com.michambita.domain.model.Producto
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ProductoRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : ProductoRepository {

    private val productoCollection = firestore.collection("productos")

    override suspend fun saveProducto(producto: Producto): Result<Unit> {
        return try {
            productoCollection.document(producto.toModel().id!!).set(producto).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getProducto(id: String): Result<Producto> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteProducto(id: String): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun getAllProductos(): Result<List<Producto>> {
        TODO("Not yet implemented")
    }
}