package com.michambita.domain.usecase

import com.michambita.domain.enums.BusinessType
import com.michambita.domain.model.Company
import com.michambita.domain.repository.AuthRepository
import com.michambita.domain.repository.CompanyRepository
import javax.inject.Inject

/**
 * Result of a registration operation. Contains the success message and the resolved BusinessType
 * (either from the created company or from the joined company).
 */
data class RegisterResult(val message: String, val businessType: BusinessType)

class RegisterUseCase
@Inject
constructor(
    private val authRepository: AuthRepository,
    private val companyRepository: CompanyRepository
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

        var companyId: String
        var isAdmin: Boolean
        var resolvedBusinessType: BusinessType

        val emailExists = authRepository.checkEmailExists(email).getOrNull()
        if (emailExists == true) {
            return Result.failure(Exception("Este correo ya está registrado"))
        }

        when (companyOption) {
            "crear" -> {
                val nombreTrimmed = companyName!!.trim()
                val existingCompany = companyRepository.getCompanyByNombre(nombreTrimmed).getOrNull()
                if (existingCompany != null) {
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
                    return Result.failure(saveResult.exceptionOrNull() ?: Exception("Error al crear la empresa"))
                }

                companyId = saveResult.getOrNull()!!
                isAdmin = true
                resolvedBusinessType = businessType!!
            }

            "asociar" -> {
                val company =
                    companyRepository.getCompanyById(companyCode!!).getOrNull()
                        ?: return Result.failure(
                            Exception("No existe una empresa con ese código")
                        )

                companyId = company.id!!
                isAdmin = false
                resolvedBusinessType = company.businessType ?: return Result.failure(
                    Exception("La empresa no tiene un tipo de negocio configurado")
                )
            }

            else -> {
                return Result.failure(Exception("Opción de empresa inválida"))
            }
        }

        val registerResult = authRepository.register(
            name = name,
            email = email,
            password = password,
            companyId = companyId,
            ctrlAdmin = isAdmin
        )

        return if (registerResult.isSuccess) {
            val message =
                when (companyOption) {
                    "crear" -> "El código identificador de su empresa es $companyId"
                    "asociar" -> "Se asoció a la empresa correctamente"
                    else -> ""
                }
            Result.success(RegisterResult(message = message, businessType = resolvedBusinessType))
        } else {
            Result.failure(registerResult.exceptionOrNull() ?: Exception("Error en el registro"))
        }
    }
}
