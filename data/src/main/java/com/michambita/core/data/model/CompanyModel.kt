package com.michambita.data.model

import com.michambita.domain.enums.BusinessType
import com.michambita.domain.model.Company

data class CompanyModel(
    val id: String? = "",
    val nombre: String = "",
    val descripcion: String? = "",
    val businessType: String? = null
)

fun CompanyModel.toDomain() = Company(
    id = id,
    nombre = nombre,
    descripcion = descripcion,
    businessType = businessType?.let {
        try { BusinessType.valueOf(it) } catch (_: IllegalArgumentException) { null }
    }
)

fun Company.toModel() = CompanyModel(
    id,
    nombre,
    descripcion,
    businessType = businessType?.name
)
