package com.michambita.data.repository.impl

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.michambita.core.data.util.Constant
import com.michambita.data.database.dao.SynchronizationDAO
import com.michambita.domain.repository.AuthRepository
import com.michambita.domain.repository.preference.CompanyPreferencesRepository
import com.michambita.domain.repository.preference.UserPreferencesRepository
import com.michambita.domain.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth,
    private val userPreferencesRepository: UserPreferencesRepository,
    private val companyPreferencesRepository: CompanyPreferencesRepository,
    private val synchronizationDAO: SynchronizationDAO,
) : AuthRepository {

    private val userCollection = firestore.collection(Constant.Documents.USUARIOS)

    override suspend fun login(email: String, password: String): Result<String> {
        return try {
            val authResult = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            val firebaseUser = authResult.user
            if (firebaseUser != null) {
                userPreferencesRepository.saveUser(User(userId = firebaseUser.uid))
                Result.success(firebaseUser.uid)
            } else {
                Result.failure(Exception("Error al iniciar sesión con Firebase: Usuario nulo"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun register(
        name: String,
        email: String,
        password: String,
        companyId: String,
        ctrlAdmin: Boolean
    ): Result<String> {
        return try {
            val authResult = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            val firebaseUser = authResult.user
            if (firebaseUser != null) {
                val userMap = hashMapOf(
                    "userId" to firebaseUser.uid,
                    "name" to name,
                    "email" to email,
                    "companyId" to companyId,
                    "ctrlAdmin" to ctrlAdmin
                )
                userCollection.document(firebaseUser.uid)
                    .set(userMap)
                    .await()
                userPreferencesRepository.saveUser(
                    User(
                        userId = firebaseUser.uid,
                        name = name,
                        email = email,
                        companyId = companyId,
                        ctrlAdmin = ctrlAdmin
                    )
                )
                Result.success(firebaseUser.uid)
            } else {
                Result.failure(Exception("Error al registrarse en Firebase"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun checkEmailExists(email: String): Result<Boolean> {
        return try {
            val query = userCollection.whereEqualTo("email", email).get().await()
            if (!query.isEmpty) {
                Result.success(true)
            } else {
                Result.success(false)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getCurrentUser(): Flow<String?> {
        return userPreferencesRepository.userFlow.map { it?.userId }
    }

    override suspend fun logout() {
        synchronizationDAO.deleteAll()
        userPreferencesRepository.clearUser()
        companyPreferencesRepository.clearCompany()
        firebaseAuth.signOut()
    }
}
