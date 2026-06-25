package com.michambita.domain.repository

import com.michambita.domain.model.Item

interface ItemRepository {
    suspend fun saveItem(item: Item): Result<Unit>
    suspend fun getItem(id: String): Result<Item>
    suspend fun deleteItem(id: String): Result<Unit>
    suspend fun getAllItemsByUserId(userId: String): Result<List<Item>>
    suspend fun updateItemStock(id: String, stock: Int): Result<Unit>
}
