package com.michambita.domain.repository

import android.net.Uri

interface ItemImageRepository {
    suspend fun uploadItemImage(uri: Uri): Result<String>
    suspend fun deleteItemImage(url: String): Result<Unit>
}
