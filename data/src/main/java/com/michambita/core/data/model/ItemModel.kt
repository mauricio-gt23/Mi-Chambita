package com.michambita.data.model

import com.michambita.domain.enums.ItemType
import com.michambita.domain.model.Item

data class ItemModel(
    val id: String? = null,
    val userId: String? = null,
    val nombre: String = "",
    val descripcion: String? = "",
    val precio: Double = 0.0,
    val itemType: String = ItemType.PRODUCT.name,
    val unidadMedida: String? = null,
    val stock: Int? = null,
    val imagenUrl: String? = null
)

fun ItemModel.toDomain() = Item(
    id = id,
    userId = userId,
    nombre = nombre,
    descripcion = descripcion,
    precio = precio,
    itemType = try { ItemType.valueOf(itemType) } catch (_: IllegalArgumentException) { ItemType.PRODUCT },
    unidadMedida = unidadMedida,
    stock = stock,
    imagenUrl = imagenUrl
)

fun Item.toModel() = ItemModel(
    id = id,
    userId = userId,
    nombre = nombre,
    descripcion = descripcion,
    precio = precio,
    itemType = itemType.name,
    unidadMedida = unidadMedida,
    stock = stock,
    imagenUrl = imagenUrl
)
