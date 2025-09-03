package com.michambita.data.model

import com.michambita.domain.model.Producto

data class ProductoModel(
    val id: String?,
    val userId: String?,
    val nombre: String,
    val descripcion: String?,
    val precio: Double,
    val unidadMedida: String,
    val esIntangible: Boolean
)

fun Producto.toModel() = ProductoModel(id, userId, nombre, descripcion, precio, unidadMedida, esIntangible)