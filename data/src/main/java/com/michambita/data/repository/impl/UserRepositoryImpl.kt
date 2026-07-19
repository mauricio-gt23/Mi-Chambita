package com.michambita.data.repository.impl

import com.google.firebase.firestore.FirebaseFirestore
import com.michambita.core.data.util.Constant
import com.michambita.data.model.UserModel
import com.michambita.data.model.toDomain
import com.michambita.domain.model.User
import com.michambita.domain.repository.UserRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    ) : UserRepository {
    private val userCollection = firestore.collection(Constant.Documents.USUARIOS)

    override suspend fun saveUserProfile(
        userId: String,
        name: String,
        email: String,
        companyId: String,
        ctrlAdmin: Boolean
    ): Result<Unit> {
        return try {
            val userMap = hashMapOf(
                "userId" to userId,
                "name" to name,
                "email" to email,
                "companyId" to companyId,
                "ctrlAdmin" to ctrlAdmin
            )
            userCollection.document(userId).set(userMap).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun fetchUser(userId: String): Result<User> {
        return try {
            val userDocument = userCollection.document(userId).get().await()
            val userModel = userDocument.toObject(UserModel::class.java)
            Result.success(userModel!!.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
