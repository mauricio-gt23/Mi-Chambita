package com.michambita.domain.usecase

import com.michambita.domain.enums.BusinessType
import com.michambita.domain.model.Company
import com.michambita.domain.model.User
import com.michambita.domain.repository.AuthRepository
import com.michambita.domain.repository.CompanyRepository
import com.michambita.domain.repository.UserRepository
import com.michambita.domain.repository.preference.CompanyPreferencesRepository
import com.michambita.domain.repository.preference.UserPreferencesRepository
import javax.inject.Inject

data class RegisterResult(val message: String, val businessType: BusinessType)

class RegisterUseCase
@Inject
constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val companyRepository: CompanyRepository,
    private val userPreferencesRepository: UserPreferencesRepository,
    private val companyPreferencesRepository: CompanyPreferencesRepository
) {

    suspend operator fun invoke(
        name: String,
        email: String,
        password: String,
        companyOption: String,
        companyName: String? = null,
        companyCode: String? = null,
        businessType: BusinessType? = null
    ): Result<RegisterResult> {

        // 1. Crear Firebase Auth
        val uid = authRepository.createAuthAccount(email, password)
            .getOrElse { return Result.failure(it) }

        // 2. Resolver la empresa (crear o asociar)
        val companyId: String
        val isAdmin: Boolean
        val resolvedBusinessType: BusinessType
        val resolvedCompany: Company

        when (companyOption) {
            "crear" -> {
                val nombreTrimmed = companyName!!.trim()
                val existingCompany = companyRepository.getCompanyByNombre(nombreTrimmed).getOrNull()
                if (existingCompany != null) {
                    authRepository.deleteAuthAccount()
                    return Result.failure(Exception("Ya existe una empresa con ese nombre"))
                }

                val newCompany =
                    Company(
                        nombre = nombreTrimmed,
                        descripcion = "",
                        businessType = businessType
                    )
                val saveResult = companyRepository.saveCompany(newCompany)
                if (saveResult.isFailure) {
                    authRepository.deleteAuthAccount()
                    return Result.failure(saveResult.exceptionOrNull() ?: Exception("Error al crear la empresa"))
                }

                companyId = saveResult.getOrNull()!!
                isAdmin = true
                resolvedBusinessType = businessType!!
                resolvedCompany = newCompany.copy(id = companyId)
            }

            "asociar" -> {
                val company = companyRepository.getCompanyById(companyCode!!).getOrNull()
                if (company == null) {
                    authRepository.deleteAuthAccount()
                    return Result.failure(Exception("No existe una empresa con ese código"))
                }
                val businessTypeEmpresa = company.businessType
                if (businessTypeEmpresa == null) {
                    authRepository.deleteAuthAccount()
                    return Result.failure(Exception("La empresa no tiene un tipo de negocio configurado"))
                }

                companyId = company.id!!
                isAdmin = false
                resolvedBusinessType = businessTypeEmpresa
                resolvedCompany = company
            }

            else -> {
                authRepository.deleteAuthAccount()
                return Result.failure(Exception("Opción de empresa inválida"))
            }
        }

        // 3. Crear usuario(Firebase)
        val profileResult = userRepository.saveUserProfile(
            userId = uid,
            name = name,
            email = email,
            companyId = companyId,
            ctrlAdmin = isAdmin
        )
        if (profileResult.isFailure) {
            authRepository.deleteAuthAccount()
            if (isAdmin) companyRepository.deleteCompanyById(companyId)
            return Result.failure(profileResult.exceptionOrNull() ?: Exception("Error al guardar el perfil"))
        }

        // 4. Crear usuario y empresa en DataS
        userPreferencesRepository.saveUser(
            User(
                userId = uid,
                name = name,
                email = email,
                companyId = companyId,
                ctrlAdmin = isAdmin
            )
        )
        companyPreferencesRepository.saveCompany(resolvedCompany)

        val message =
            when (companyOption) {
                "crear" -> "El código identificador de su empresa es $companyId"
                "asociar" -> "Se asoció a la empresa correctamente"
                else -> ""
            }
        return Result.success(RegisterResult(message = message, businessType = resolvedBusinessType))
    }
}
