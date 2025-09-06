package com.michambita.domain.model

import com.michambita.data.database.entity.MovimientoEntity
import java.math.BigDecimal
import java.util.Date

data class Movimiento(
    val id: Long? = null,
    var userId: String? = "",
    val descripcion: String,
    val monto: BigDecimal,
    val tipoMovimiento: String, // "V" o "G"
    val fechaRegistro: Date = Date(),
    val sincronizado: Boolean = false
)

fun MovimientoEntity.toDomain() = Movimiento(id, userId, descripcion, monto, tipoMovimiento, fechaRegistro, sincronizado)