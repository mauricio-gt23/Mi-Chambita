package com.michambita.domain.model

import com.michambita.data.enums.EnumTipoProducto
import com.michambita.data.model.ProductoModel

data class Producto(
    val id: String? = "",
    var userId: String? = "",
    val nombre: String,
    val descripcion: String? = "",
    val precio: Double,
    val unidadMedida: String,
    val tipoProducto: EnumTipoProducto,
    val stock: Int? = null,
    val imagenUrl: String? = null
)

fun ProductoModel.toDomain() = Producto(
    id,
    userId,
    nombre,
    descripcion,
    precio,
    unidadMedida,
    tipoProducto,
    stock,
    imagenUrl
)