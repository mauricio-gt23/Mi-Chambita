package com.michambita.data.repository.impl

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.michambita.data.local.preferences.UserPreferencesRepository // Asegúrate que la ruta es correcta
import com.michambita.data.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth,
    private val userPreferencesRepository: UserPreferencesRepository
) : AuthRepository {

    override suspend fun login(email: String, password: String): Result<String> {
        return try {
            val authResult = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            val firebaseUser = authResult.user
            if (firebaseUser != null) {
                userPreferencesRepository.saveUserUid(firebaseUser.uid)
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
        password: String
    ): Result<String> {
        return try {
            val authResult = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            val firebaseUser = authResult.user
            if (firebaseUser != null) {
                val userMap = hashMapOf(
                    "userId" to firebaseUser.uid,
                    "name" to name,
                    "email" to email
                )
                firestore.collection("users").document(firebaseUser.uid)
                    .set(userMap)
                    .await()
                userPreferencesRepository.saveUserUid(firebaseUser.uid)
                Result.success(firebaseUser.uid)
            } else {
                Result.failure(Exception("Error al registrarse en Firebase"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getCurrentUser(): Flow<String?> {
        return userPreferencesRepository.userUidFlow
    }

    override suspend fun logout() {
        userPreferencesRepository.clearUserUid()
        firebaseAuth.signOut()
    }
}