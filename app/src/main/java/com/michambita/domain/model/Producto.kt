package com.michambita.domain.model

import com.michambita.data.model.ProductoModel

data class Producto(
    val id: String? = "",
    var userId: String? = "",
    val nombre: String,
    val descripcion: String? = "",
    val precio: Double,
    val unidadMedida: String,
    val esIntangible: Boolean
)

fun ProductoModel.toModel() = ProductoModel(id, userId, nombre, descripcion, precio, unidadMedida, esIntangible)