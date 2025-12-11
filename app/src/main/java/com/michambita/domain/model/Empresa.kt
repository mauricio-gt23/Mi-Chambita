package com.michambita.domain.model

import com.michambita.data.model.EmpresaModel

data class Empresa(
    val id: String? = null,
    val nombre: String,
    val descripcion: String? = "",
    val idUserAdm: Long
)

fun Empresa.toModel() = EmpresaModel(
    id,
    nombre,
    descripcion,
    idUserAdm
)