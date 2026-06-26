package com.michambita.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.michambita.domain.enums.EnumTipoMovimiento
import com.michambita.domain.model.Movimiento
import com.michambita.domain.model.MovimientoItem
import java.math.BigDecimal
import java.util.Date

@Entity(tableName = "movimiento")
data class MovimientoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "company_id")
    val companyId: String = "",

    @ColumnInfo(name = "created_by_user_id")
    val createdByUserId: String? = null,

    @ColumnInfo(name = "descripcion")
    val descripcion: String = "",

    @ColumnInfo(name = "monto")
    val monto: BigDecimal = BigDecimal.ZERO,

    @ColumnInfo(name = "tipo_movimiento")
    val tipoMovimiento: EnumTipoMovimiento = EnumTipoMovimiento.EXPENSE,

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
    MovimientoEntity(this.id ?: 0, companyId!!, createdByUserId, descripcion, monto, tipoMovimiento, fechaRegistro, sincronizado, items, esMovimientoRapido)

fun MovimientoEntity.toDomain(): Movimiento =
    Movimiento(this.id, companyId, createdByUserId, descripcion, monto, tipoMovimiento, fechaRegistro, sincronizado, esMovimientoRapido, items)
