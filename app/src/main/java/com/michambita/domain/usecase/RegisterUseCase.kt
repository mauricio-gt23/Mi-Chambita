package com.michambita.domain.usecase

import com.michambita.data.repository.AuthRepository
import com.michambita.data.repository.EmpresaRepository
import com.michambita.domain.model.Empresa
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val empresaRepository: EmpresaRepository
) {

    suspend operator fun invoke(
        name: String,
        email: String,
        password: String,
        empresaOption: String,
        empresaNombre: String? = null,
        empresaCodigo: String? = null
    ): Result<String> {

        var empresaId: String
        var isAdmin: Boolean

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

                val nuevaEmpresa = Empresa(
                    nombre = nombreTrimmed,
                    descripcion = ""
                )
                val saveResult = empresaRepository.saveEmpresa(nuevaEmpresa)
                if (saveResult.isFailure) {
                    return Result.failure(saveResult.exceptionOrNull() ?: Exception("Error al crear la empresa"))
                }

                empresaId = saveResult.getOrNull()!!
                isAdmin = true
            }
            "asociar" -> {
                val empresa = empresaRepository.getEmpresaById(empresaCodigo!!).getOrNull()
                    ?: return Result.failure(Exception("No existe una empresa con ese código"))

                empresaId = empresa.id!!
                isAdmin = false
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
            when (empresaOption) {
                "crear" -> Result.success("El código identificador de su empresa es $empresaId", )
                "asociar" -> Result.success("Se asoció a la empresa correctamente")
                else -> Result.failure(Exception("Opción de empresa inválida"))
            }
        } else {
            registerResult
        }
    }
}