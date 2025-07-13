package com.michambita.domain.model

import java.math.BigDecimal

data class Movimiento(
    val descripcion: String,
    val monto: BigDecimal,
    val tipoMovimiento: String
)