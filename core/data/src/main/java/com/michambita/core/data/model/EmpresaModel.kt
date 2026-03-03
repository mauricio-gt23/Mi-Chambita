package com.michambita.core.data.model

import com.michambita.core.domain.model.Empresa

data class EmpresaModel(
    val id: String? = "",
    val nombre: String = "",
    val descripcion: String? = ""
)

fun EmpresaModel.toDomain() = Empresa(
    id = id,
    nombre = nombre,
    descripcion = descripcion
)

fun Empresa.toModel() = EmpresaModel(
    id,
    nombre,
    descripcion
)
