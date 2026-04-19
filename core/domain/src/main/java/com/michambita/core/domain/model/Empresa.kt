package com.michambita.core.domain.model

import com.michambita.core.domain.enums.BusinessType

data class Empresa(
    val id: String? = null,
    val nombre: String,
    val descripcion: String? = "",
    val businessType: BusinessType? = null
)
