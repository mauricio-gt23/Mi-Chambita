package com.michambita.data.repository.impl

import com.google.firebase.firestore.FirebaseFirestore
import com.michambita.data.model.CompanyModel
import com.michambita.data.model.toDomain
import com.michambita.data.model.toModel
import com.michambita.domain.model.Company
import com.michambita.domain.repository.CompanyRepository
import kotlinx.coroutines.tasks.await
import java.util.UUID
import javax.inject.Inject

class CompanyRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : CompanyRepository {

    private val companyCollection = firestore.collection("companies")

    override suspend fun saveCompany(company: Company): Result<String> {
        return try {
            val id = if (company.id.isNullOrBlank()) {
                UUID.randomUUID().toString().substring(0, 8)
            } else {
                company.id
            }

            val companyWithId = company.copy(id = id)
            companyCollection.document(id!!).set(companyWithId.toModel()).await()
            Result.success(companyWithId.id!!)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getCompanyByNombre(nombre: String): Result<Company?> {
        return try {
            val querySnapshot = companyCollection
                .whereEqualTo("nombre", nombre)
                .limit(1)
                .get()
                .await()

            if (querySnapshot.isEmpty) {
                Result.success(null)
            } else {
                val companyModel = querySnapshot.documents[0].toObject(CompanyModel::class.java)
                Result.success(companyModel?.toDomain())
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getCompanyById(id: String): Result<Company?> {
        return try {
            val documentSnapshot = companyCollection.document(id).get().await()

            if (documentSnapshot.exists()) {
                val companyModel = documentSnapshot.toObject(CompanyModel::class.java)
                Result.success(companyModel?.toDomain())
            } else {
                Result.success(null)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
