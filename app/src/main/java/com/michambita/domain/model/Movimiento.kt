package com.michambita.domain.model

import java.math.BigDecimal
import java.util.Date

data class Movimiento(
    val id: Long? = null,
    val userId: String? = "",
    val descripcion: String,
    val monto: BigDecimal,
    val tipoMovimiento: String, // "V" o "G"
    val fechaRegistro: Date = Date(),
    val sincronizado: Boolean = false
)