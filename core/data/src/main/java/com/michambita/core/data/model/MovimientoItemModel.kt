package com.michambita.core.data.model

import com.michambita.core.domain.model.MovimientoItem

data class MovimientoItemModel(
    val productoId: String = "",
    val cantidad: Int = 0,
    val precioTotal: Double = 0.0
)

fun MovimientoItemModel.toDomain() = MovimientoItem(
    productoId,
    cantidad,
    precioTotal.toBigDecimal()
)

fun MovimientoItem.toModel() = MovimientoItemModel(
    productoId,
    cantidad,
    precioTotal.toDouble()
)
