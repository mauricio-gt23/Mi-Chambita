package com.michambita.data.model

import com.michambita.domain.model.MovimientoItem

data class MovimientoItemModel(
    val itemId: String = "",
    val cantidad: Int = 0,
    val precioTotal: Double = 0.0
)

fun MovimientoItemModel.toDomain() = MovimientoItem(
    itemId,
    cantidad,
    precioTotal.toBigDecimal()
)

fun MovimientoItem.toModel() = MovimientoItemModel(
    itemId,
    cantidad,
    precioTotal.toDouble()
)
