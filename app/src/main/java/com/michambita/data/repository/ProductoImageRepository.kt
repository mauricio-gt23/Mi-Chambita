package com.michambita.data.repository

import android.net.Uri

interface ProductoImageRepository {
    suspend fun uploadProductoImage(uri: Uri): Result<String>
}