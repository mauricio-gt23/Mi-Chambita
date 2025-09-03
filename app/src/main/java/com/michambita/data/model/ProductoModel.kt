package com.michambita.data.model

import com.michambita.domain.model.Producto

data class ProductoModel(
    val id: String? = null,
    val userId: String? = null,
    val nombre: String = "",
    val descripcion: String? = "",
    val precio: Double = 0.0,
    val unidadMedida: String = "",
    val esIntangible: Boolean = false
)

fun Producto.toModel() = ProductoModel(id, userId, nombre, descripcion, precio, unidadMedida, esIntangible)