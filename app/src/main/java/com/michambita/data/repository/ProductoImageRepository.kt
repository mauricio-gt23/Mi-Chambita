package com.michambita.data.repository

import android.net.Uri

interface ProductoImageRepository {
    suspend fun uploadProductoImage(uri: Uri): Result<String>
    suspend fun deleteProductoImage(url: String): Result<Unit>
}