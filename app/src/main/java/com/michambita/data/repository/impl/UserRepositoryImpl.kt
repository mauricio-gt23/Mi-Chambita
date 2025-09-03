package com.michambita.data.repository.impl

import com.google.firebase.firestore.FirebaseFirestore
import com.michambita.data.model.UserModel
import com.michambita.data.repository.UserRepository
import com.michambita.domain.model.User
import com.michambita.domain.model.toDomain
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