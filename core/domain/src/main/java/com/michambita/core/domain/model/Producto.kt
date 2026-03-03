package com.michambita.core.domain.model

import com.michambita.core.domain.enums.EnumTipoProducto

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
