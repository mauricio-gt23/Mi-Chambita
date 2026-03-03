package com.michambita.core.data.model

import com.michambita.core.domain.enums.EnumTipoMovimiento
import com.michambita.core.domain.model.Movimiento
import com.michambita.core.domain.model.MovimientoItem
import java.math.BigDecimal
import java.util.Date

data class MovimientoModel(
    val id: String? = null,
    val userId: String? = null,
    val descripcion: String = "",
    val monto: Double = 0.0,
    val tipoMovimiento: EnumTipoMovimiento = EnumTipoMovimiento.GASTO,
    val fechaRegistro: Date = Date(),
    val sincronizado: Boolean = false,
    val esMovimientoRapido: Boolean = true,
    val items: List<MovimientoItemModel> = emptyList()
)

fun MovimientoModel.toDomain() = Movimiento(
    id = null,
    userId = userId,
    descripcion = descripcion,
    monto = BigDecimal.valueOf(monto),
    tipoMovimiento = tipoMovimiento,
    fechaRegistro = fechaRegistro,
    sincronizado = sincronizado,
    esMovimientoRapido = esMovimientoRapido,
    items = items.map { item -> item.toDomain() }
)

fun Movimiento.toModel() = MovimientoModel(
    id = null,
    userId = userId,
    descripcion = descripcion,
    monto = monto.toDouble(),
    tipoMovimiento = tipoMovimiento,
    fechaRegistro = fechaRegistro,
    sincronizado = sincronizado,
    esMovimientoRapido = esMovimientoRapido,
    items = items.map { item -> item.toModel() }
)
