package com.michambita.data.repository.impl

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import com.michambita.data.repository.ProductoImageRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ProductoImageRepositoryImpl @Inject constructor(
    private val firebaseStorage: FirebaseStorage
) : ProductoImageRepository {

    override suspend fun uploadProductoImage(uri: Uri): Result<String> {
        return try {
            val storageRef = firebaseStorage.reference
            val imageRef = storageRef.child("productos/${System.currentTimeMillis()}.jpg")
            imageRef.putFile(uri).await()
            val url = imageRef.downloadUrl.await().toString()
            Result.success(url)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}