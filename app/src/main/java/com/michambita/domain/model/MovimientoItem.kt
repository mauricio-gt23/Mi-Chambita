package com.michambita.domain.model

import MovimientoItemModel
import java.math.BigDecimal

data class MovimientoItem(
    val productoId: String,
    val cantidad: Int,
    val precioTotal: BigDecimal
)

fun MovimientoItemModel.toDomain() = MovimientoItem(
    productoId,
    cantidad,
    precioTotal.toBigDecimal()
)