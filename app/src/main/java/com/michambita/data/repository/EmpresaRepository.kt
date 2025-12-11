package com.michambita.data.repository

import com.michambita.domain.model.Empresa

interface EmpresaRepository {
    suspend fun saveEmpresa(empresa: Empresa): Result<Unit>
}