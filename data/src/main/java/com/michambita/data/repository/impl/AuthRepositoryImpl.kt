package com.michambita.data.repository.impl

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.michambita.data.database.dao.SynchronizationDAO
import com.michambita.domain.repository.AuthRepository
import com.michambita.domain.repository.preference.CompanyPreferencesRepository
import com.michambita.domain.repository.preference.UserPreferencesRepository
import com.michambita.domain.model.User
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val userPreferencesRepository: UserPreferencesRepository,
    private val companyPreferencesRepository: CompanyPreferencesRepository,
    private val synchronizationDAO: SynchronizationDAO,
) : AuthRepository {
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

    override suspend fun createAuthAccount(email: String, password: String): Result<String> {
        return try {
            val authResult = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            val uid = authResult.user?.uid
                ?: return Result.failure(Exception("Error al registrarse en Firebase"))
            Result.success(uid)
        } catch (e: FirebaseAuthUserCollisionException) {
            Result.failure(Exception("Este correo ya está registrado"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteAuthAccount(): Result<Unit> {
        return try {
            firebaseAuth.currentUser?.delete()?.await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun sendPasswordResetEmail(email: String): Result<Unit> {
        return try {
            firebaseAuth.sendPasswordResetEmail(email).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun logout() {
        synchronizationDAO.deleteAll()
        userPreferencesRepository.clearUser()
        companyPreferencesRepository.clearCompany()
        firebaseAuth.signOut()
    }
}
