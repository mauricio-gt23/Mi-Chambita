package com.michambita.domain.model

import com.michambita.domain.enums.EnumTipoMovimiento
import java.math.BigDecimal
import java.util.Date

data class Movimiento(
    val id: String? = null,
    var companyId: String? = "",
    val createdByUserId: String? = null,
    val descripcion: String,
    val monto: BigDecimal,
    val tipoMovimiento: EnumTipoMovimiento,
    val fechaRegistro: Date = Date(),
    val sincronizado: Boolean = false,
    val esMovimientoRapido: Boolean = true,
    val items: List<MovimientoItem> = emptyList()
)
