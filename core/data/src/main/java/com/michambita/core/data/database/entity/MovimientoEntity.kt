package com.michambita.core.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.michambita.core.domain.enums.EnumTipoMovimiento
import com.michambita.core.domain.model.Movimiento
import com.michambita.core.domain.model.MovimientoItem
import java.math.BigDecimal
import java.util.Date

@Entity(tableName = "movimiento")
data class MovimientoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "user_id")
    val userId: String = "",

    @ColumnInfo(name = "descripcion")
    val descripcion: String = "",

    @ColumnInfo(name = "monto")
    val monto: BigDecimal = BigDecimal.ZERO,

    @ColumnInfo(name = "tipo_movimiento")
    val tipoMovimiento: EnumTipoMovimiento = EnumTipoMovimiento.GASTO,

    @ColumnInfo(name = "fecha_registro")
    val fechaRegistro: Date = Date(),

    @ColumnInfo(name = "sincronizado")
    val sincronizado: Boolean = false,

    @ColumnInfo(name = "items")
    val items: List<MovimientoItem> = emptyList(),

    @ColumnInfo(name = "es_movimiento_rapido")
    val esMovimientoRapido: Boolean = true
)

fun Movimiento.toDataBase(): MovimientoEntity =
    MovimientoEntity(this.id ?: 0, userId!!, descripcion, monto, tipoMovimiento, fechaRegistro, sincronizado, items, esMovimientoRapido)

fun MovimientoEntity.toDomain(): Movimiento =
    Movimiento(this.id, userId, descripcion, monto, tipoMovimiento, fechaRegistro, sincronizado, esMovimientoRapido, items)
