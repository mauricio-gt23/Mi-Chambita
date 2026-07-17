package com.michambita.domain.repository

interface ItemImageRepository {
    suspend fun uploadItemImage(uriString: String): Result<String>
    suspend fun deleteItemImage(url: String): Result<Unit>
}
