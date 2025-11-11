package com.michambita.data.model

import com.michambita.data.enum.EnumTipoProducto
import com.michambita.domain.model.Producto

data class ProductoModel(
    val id: String? = null,
    val userId: String? = null,
    val nombre: String = "",
    val descripcion: String? = "",
    val precio: Double = 0.0,
    val unidadMedida: String = "",
    val tipoProducto: EnumTipoProducto = EnumTipoProducto.NO_INVENTARIABLE,
    val stock: Int? = null
)

fun Producto.toModel() = ProductoModel(
    id,
    userId,
    nombre,
    descripcion,
    precio,
    unidadMedida,
    tipoProducto,
    stock
)