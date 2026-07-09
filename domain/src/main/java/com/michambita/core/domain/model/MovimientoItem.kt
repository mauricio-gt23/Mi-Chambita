package com.michambita.domain.model

import java.math.BigDecimal

data class MovimientoItem(
    val itemId: String,
    val cantidad: Int,
    val precioTotal: BigDecimal
)
