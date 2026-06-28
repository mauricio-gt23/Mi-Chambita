package com.michambita.data.repository.impl

import com.google.firebase.firestore.FirebaseFirestore
import com.michambita.core.data.util.Constant
import com.michambita.data.model.ItemModel
import com.michambita.data.model.toDomain
import com.michambita.data.model.toModel
import com.michambita.domain.model.Item
import com.michambita.domain.repository.ItemRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ItemRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : ItemRepository {

    private val itemCollection = firestore.collection(Constant.Documents.ITEMS)

    override suspend fun saveItem(item: Item): Result<Unit> {
        return try {
            val id = item.id
            if (id.isNullOrBlank()) {
                itemCollection.document().set(item.toModel()).await()
            } else {
                itemCollection.document(id).set(item.toModel()).await()
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getItem(id: String): Result<Item> {
        return try {
            val doc = itemCollection.document(id).get().await()
            val model = doc.toObject(ItemModel::class.java)
            if (model != null) {
                Result.success(model.copy(id = doc.id).toDomain())
            } else {
                Result.failure(IllegalStateException("Item no encontrado"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteItem(id: String): Result<Unit> {
        return try {
            itemCollection.document(id).delete().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getAllItemsByCompanyId(companyId: String): Result<List<Item>> {
        return try {
            val querySnapshot = itemCollection.whereEqualTo("companyId", companyId).get().await()
            val itemList = querySnapshot.documents.mapNotNull { document ->
                document.toObject(ItemModel::class.java)?.copy(id = document.id)?.toDomain()
            }
            Result.success(itemList)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateItemStock(id: String, stock: Int): Result<Unit> {
        return try {
            itemCollection.document(id).update("stock", stock).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
