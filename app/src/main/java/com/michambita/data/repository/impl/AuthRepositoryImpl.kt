package com.michambita.data.repository.impl

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.actionCodeSettings
import com.michambita.data.repository.AuthRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
) : AuthRepository {

    override suspend fun loginWithEmailLink(email: String): Result<String> {
        return try {
            Result.success("hola")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun loginWithPhoneNumber(phone: String): Result<Unit> {
        // Aquí va la lógica con PhoneAuthProvider (Firebase Auth)
        return Result.failure(NotImplementedError("Login con teléfono aún no implementado"))
    }
}