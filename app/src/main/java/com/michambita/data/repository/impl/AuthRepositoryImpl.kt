package com.michambita.data.repository.impl

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.michambita.data.repository.AuthRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth
) : AuthRepository {

    override suspend fun login(email: String, password: String): Result<String> {
        return try {
            val authResult = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            val firebaseUser = authResult.user
            if (firebaseUser != null) {
                Result.success(firebaseUser.uid)
            } else {
                Result.failure(Exception("Error al iniciar sesión con Firebase"))
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
                Result.success(firebaseUser.uid)
            } else {
                Result.failure(Exception("Error al registrarse en Firebase"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}