package com.michambita.data.repository.impl

import com.google.firebase.firestore.FirebaseFirestore
import com.michambita.data.model.toModel
import com.michambita.data.repository.EmpresaRepository
import com.michambita.domain.model.Empresa
import com.michambita.domain.model.toModel
import kotlinx.coroutines.tasks.await
import java.util.UUID
import javax.inject.Inject

class EmpresaRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
): EmpresaRepository {

    private val empresaCollection = firestore.collection("empresas")

    override suspend fun saveEmpresa(empresa: Empresa): Result<Unit> {
        return try {
            val id = if (empresa.id.isNullOrBlank()) {
                UUID.randomUUID().toString().substring(0, 8)
            } else {
                empresa.id
            }
            
            val empresaWithId = empresa.copy(id = id)
            empresaCollection.document(id).set(empresaWithId.toModel()).await()
            
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}