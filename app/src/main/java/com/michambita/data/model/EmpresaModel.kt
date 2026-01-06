package com.michambita.data.model

import com.michambita.domain.model.Empresa

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