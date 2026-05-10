package com.michambita.domain.repository

import com.michambita.domain.model.Empresa

interface EmpresaRepository {
    suspend fun saveEmpresa(empresa: Empresa): Result<String>
    suspend fun getEmpresaByNombre(nombre: String): Result<Empresa?>
    suspend fun getEmpresaById(id: String): Result<Empresa?>
}
