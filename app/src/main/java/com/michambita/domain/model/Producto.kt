package com.michambita.domain.model

data class Producto(
    val id: String? = "",
    val nombre: String,
    val descripcion: String? = "",
    val precio: Double,
    val unidadMedida: String,
    val esIntangible: Boolean
)