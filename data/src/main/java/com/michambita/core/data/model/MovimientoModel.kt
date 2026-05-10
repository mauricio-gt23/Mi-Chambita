package com.michambita.data.model

import com.michambita.domain.enums.EnumTipoMovimiento
import com.michambita.domain.model.Movimiento
import java.util.Date

data class MovimientoModel(
    val id: Long? = null,
    val userId: String? = "",
    val descripcion: String = "",
    val monto: Double = 0.0,
    val tipoMovimiento: String = EnumTipoMovimiento.INCOME.name,
    val fechaRegistro: Date = Date(),
    val sincronizado: Boolean = false,
    val esMovimientoRapido: Boolean = true,
    val items: List<MovimientoItemModel> = emptyList()
)

fun MovimientoModel.toDomain() = Movimiento(
    id = id,
    userId = userId,
    descripcion = descripcion,
    monto = monto.toBigDecimal(),
    tipoMovimiento = try {
        EnumTipoMovimiento.valueOf(tipoMovimiento)
    } catch (e: Exception) {
        EnumTipoMovimiento.INCOME
    },
    fechaRegistro = fechaRegistro,
    sincronizado = sincronizado,
    esMovimientoRapido = esMovimientoRapido,
    items = items.map { it.toDomain() }
)

fun Movimiento.toModel() = MovimientoModel(
    id = id,
    userId = userId,
    descripcion = descripcion,
    monto = monto.toDouble(),
    tipoMovimiento = tipoMovimiento.name,
    fechaRegistro = fechaRegistro,
    sincronizado = sincronizado,
    esMovimientoRapido = esMovimientoRapido,
    items = items.map { it.toModel() }
)
