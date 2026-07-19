package com.michambita.domain.repository

import com.michambita.domain.model.Company

interface CompanyRepository {
    suspend fun saveCompany(company: Company): Result<String>
    suspend fun getCompanyByNombre(nombre: String): Result<Company?>
    suspend fun getCompanyById(id: String): Result<Company?>
    suspend fun deleteCompanyById(id: String): Result<Unit>
}
