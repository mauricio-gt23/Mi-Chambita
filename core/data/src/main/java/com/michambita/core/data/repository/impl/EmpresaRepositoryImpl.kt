package com.michambita.core.data.repository.impl

import com.google.firebase.firestore.FirebaseFirestore
import com.michambita.core.data.model.EmpresaModel
import com.michambita.core.data.model.toDomain
import com.michambita.core.data.model.toModel
import com.michambita.core.domain.model.Empresa
import com.michambita.core.domain.repository.EmpresaRepository
import kotlinx.coroutines.tasks.await
import java.util.UUID
import javax.inject.Inject

class EmpresaRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : EmpresaRepository {

    private val empresaCollection = firestore.collection("empresas")

    override suspend fun saveEmpresa(empresa: Empresa): Result<String> {
        return try {
            val id = if (empresa.id.isNullOrBlank()) {
                UUID.randomUUID().toString().substring(0, 8)
            } else {
                empresa.id
            }

            val empresaWithId = empresa.copy(id = id)
            empresaCollection.document(id!!).set(empresaWithId.toModel()).await()
            Result.success(empresaWithId.id!!)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getEmpresaByNombre(nombre: String): Result<Empresa?> {
        return try {
            val querySnapshot = empresaCollection
                .whereEqualTo("nombre", nombre)
                .limit(1)
                .get()
                .await()

            if (querySnapshot.isEmpty) {
                Result.success(null)
            } else {
                val empresaModel = querySnapshot.documents[0].toObject(EmpresaModel::class.java)
                Result.success(empresaModel?.toDomain())
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getEmpresaById(id: String): Result<Empresa?> {
        return try {
            val documentSnapshot = empresaCollection.document(id).get().await()

            if (documentSnapshot.exists()) {
                val empresaModel = documentSnapshot.toObject(EmpresaModel::class.java)
                Result.success(empresaModel?.toDomain())
            } else {
                Result.success(null)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
