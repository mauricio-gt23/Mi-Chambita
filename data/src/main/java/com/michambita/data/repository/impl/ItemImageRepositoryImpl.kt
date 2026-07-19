package com.michambita.data.repository.impl

import com.google.firebase.storage.FirebaseStorage
import com.michambita.core.data.util.Constant
import com.michambita.domain.repository.ItemImageRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import androidx.core.net.toUri

class ItemImageRepositoryImpl @Inject constructor(
    private val firebaseStorage: FirebaseStorage
) : ItemImageRepository {

    private val itemsPath = Constant.Documents.ITEMS

    override suspend fun uploadItemImage(uriString: String): Result<String> {
        return try {
            val uri = uriString.toUri()
            val storageRef = firebaseStorage.reference
            val imageRef = storageRef.child("${itemsPath}/${System.currentTimeMillis()}.jpg")
            imageRef.putFile(uri).await()
            val url = imageRef.downloadUrl.await().toString()
            Result.success(url)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteItemImage(url: String): Result<Unit> {
        return try {
            val ref = firebaseStorage.getReferenceFromUrl(url)
            ref.delete().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
