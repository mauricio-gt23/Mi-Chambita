package com.michambita.data.repository.impl

import com.google.firebase.firestore.FirebaseFirestore
import com.michambita.data.model.ProductoModel
import com.michambita.data.model.toModel // For saveProducto
import com.michambita.domain.model.toDomain // For getAllProductosByUserId
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
            val id = producto.id
            if (id.isNullOrBlank()) {
                productoCollection.document().set(producto.toModel()).await()
            } else {
                productoCollection.document(id).set(producto.toModel()).await()
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getProducto(id: String): Result<Producto> {
        return try {
            val doc = productoCollection.document(id).get().await()
            val model = doc.toObject(ProductoModel::class.java)
            if (model != null) {
                Result.success(model.copy(id = doc.id).toDomain())
            } else {
                Result.failure(IllegalStateException("Producto no encontrado"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteProducto(id: String): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun getAllProductosByUserId(userId: String): Result<List<Producto>> {
        return try {
            val querySnapshot = productoCollection.whereEqualTo("userId", userId).get().await()
            val productoList = querySnapshot.documents.mapNotNull { document ->
                document.toObject(ProductoModel::class.java)?.copy(id = document.id)?.toDomain()
            }
            Result.success(productoList)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateProductoStock(id: String, stock: Int): Result<Unit> {
        return try {
            productoCollection.document(id).update("stock", stock).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}