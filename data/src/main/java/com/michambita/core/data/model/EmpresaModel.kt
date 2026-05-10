package com.michambita.data.model

import com.michambita.domain.enums.BusinessType
import com.michambita.domain.model.Empresa

data class EmpresaModel(
    val id: String? = "",
    val nombre: String = "",
    val descripcion: String? = "",
    val businessType: String? = null
)

fun EmpresaModel.toDomain() = Empresa(
    id = id,
    nombre = nombre,
    descripcion = descripcion,
    businessType = businessType?.let {
        try { BusinessType.valueOf(it) } catch (_: IllegalArgumentException) { null }
    }
)

fun Empresa.toModel() = EmpresaModel(
    id,
    nombre,
    descripcion,
    businessType = businessType?.name
)
