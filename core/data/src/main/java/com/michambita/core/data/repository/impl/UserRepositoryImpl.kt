package com.michambita.core.data.repository.impl

import com.google.firebase.firestore.FirebaseFirestore
import com.michambita.core.data.model.UserModel
import com.michambita.core.data.model.toDomain
import com.michambita.core.domain.model.User
import com.michambita.core.domain.repository.UserRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : UserRepository {
    private val userCollection = firestore.collection("users")

    override suspend fun getUser(userId: String): Result<User> {
        return try {
            val userDocument = userCollection.document(userId).get().await()
            val userModel = userDocument.toObject(UserModel::class.java)
            Result.success(userModel!!.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
