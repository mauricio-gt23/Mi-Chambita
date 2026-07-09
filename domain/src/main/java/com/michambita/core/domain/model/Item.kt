package com.michambita.domain.model

import com.michambita.domain.enums.ItemType

data class Item(
    val id: String? = "",
    var companyId: String? = "",
    val nombre: String,
    val descripcion: String? = "",
    val precio: Double,
    val itemType: ItemType,
    val unidadMedida: String? = null,
    val stock: Int? = null,
    val imagenUrl: String? = null
)
