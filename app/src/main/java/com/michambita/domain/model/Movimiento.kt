package com.michambita.domain.model

import com.michambita.data.database.entity.MovimientoEntity
import com.michambita.data.enums.EnumTipoMovimiento
import java.math.BigDecimal
import java.util.Date

data class Movimiento(
    val id: Long? = null,
    var userId: String? = "",
    val descripcion: String,
    val monto: BigDecimal,
    val tipoMovimiento: EnumTipoMovimiento,
    val fechaRegistro: Date = Date(),
    val sincronizado: Boolean = false,
    val esMovimientoRapido: Boolean = true,
    val items: List<MovimientoItem> = emptyList()
)

fun MovimientoEntity.toDomain(): Movimiento =
    Movimiento(this.id, userId, descripcion, monto, tipoMovimiento, fechaRegistro, sincronizado, esMovimientoRapido, items)
