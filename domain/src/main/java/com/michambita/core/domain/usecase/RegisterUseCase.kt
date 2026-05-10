package com.michambita.domain.usecase

import com.michambita.domain.enums.BusinessType
import com.michambita.domain.model.Empresa
import com.michambita.domain.repository.AuthRepository
import com.michambita.domain.repository.EmpresaRepository
import javax.inject.Inject

/**
 * Result of a registration operation. Contains the success message and the resolved BusinessType
 * (either from the created empresa or from the joined empresa).
 */
data class RegisterResult(val message: String, val businessType: BusinessType)

class RegisterUseCase
@Inject
constructor(
    private val authRepository: AuthRepository,
    private val empresaRepository: EmpresaRepository
) {

    suspend operator fun invoke(
        name: String,
        email: String,
        password: String,
        empresaOption: String,
        empresaNombre: String? = null,
        empresaCodigo: String? = null,
        businessType: BusinessType? = null
    ): Result<RegisterResult> {

        var empresaId: String
        var isAdmin: Boolean
        var resolvedBusinessType: BusinessType

        val emailExists = authRepository.checkEmailExists(email).getOrNull()
        if (emailExists == true) {
            return Result.failure(Exception("Este correo ya está registrado"))
        }

        when (empresaOption) {
            "crear" -> {
                val nombreTrimmed = empresaNombre!!.trim()
                val existingEmpresa = empresaRepository.getEmpresaByNombre(nombreTrimmed).getOrNull()
                if (existingEmpresa != null) {
                    return Result.failure(Exception("Ya existe una empresa con ese nombre"))
                }

                val nuevaEmpresa =
                    Empresa(
                        nombre = nombreTrimmed,
                        descripcion = "",
                        businessType = businessType
                    )
                val saveResult = empresaRepository.saveEmpresa(nuevaEmpresa)
                if (saveResult.isFailure) {
                    return Result.failure(saveResult.exceptionOrNull() ?: Exception("Error al crear la empresa"))
                }

                empresaId = saveResult.getOrNull()!!
                isAdmin = true
                resolvedBusinessType = businessType!!
            }

            "asociar" -> {
                val empresa =
                    empresaRepository.getEmpresaById(empresaCodigo!!).getOrNull()
                        ?: return Result.failure(
                            Exception("No existe una empresa con ese código")
                        )

                empresaId = empresa.id!!
                isAdmin = false
                resolvedBusinessType = empresa.businessType ?: return Result.failure(
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
            idEmpresa = empresaId,
            ctrlAdmin = isAdmin
        )

        return if (registerResult.isSuccess) {
            val message =
                when (empresaOption) {
                    "crear" -> "El código identificador de su empresa es $empresaId"
                    "asociar" -> "Se asoció a la empresa correctamente"
                    else -> ""
                }
            Result.success(RegisterResult(message = message, businessType = resolvedBusinessType))
        } else {
            Result.failure(registerResult.exceptionOrNull() ?: Exception("Error en el registro"))
        }
    }
}
