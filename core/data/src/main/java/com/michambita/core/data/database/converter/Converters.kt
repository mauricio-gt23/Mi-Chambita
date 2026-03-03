package com.michambita.core.data.database.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.math.BigDecimal
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter
import java.util.Date
import com.michambita.core.domain.enums.EnumTipoMovimiento
import com.michambita.core.domain.model.MovimientoItem

class Converters {

    private val gson = Gson()
    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

    @TypeConverter
    fun fromDate(date: Date?): Long? = date?.time

    @TypeConverter
    fun toDate(timestamp: Long?): Date? = timestamp?.let { Date(it) }

    @TypeConverter
    fun toLocalDateTime(value: String?): LocalDateTime? =
        value?.let { LocalDateTime.parse(it, formatter) }

    @TypeConverter
    fun fromLocalDateTime(dateTime: LocalDateTime?): String? = dateTime?.format(formatter)

    @TypeConverter
    fun fromBigDecimal(input: BigDecimal?): Double? = input?.toDouble()

    @TypeConverter
    fun toBigDecimal(input: Double?): BigDecimal? = input?.let { BigDecimal.valueOf(it) }

    @TypeConverter
    fun fromLong(value: Long?): LocalDate? = value?.let { LocalDate.ofEpochDay(it) }

    @TypeConverter
    fun toLong(date: LocalDate?): Long? = date?.toEpochDay()

    @TypeConverter
    fun fromStringList(list: List<String>?): String? = gson.toJson(list)

    @TypeConverter
    fun toStringList(value: String?): List<String>? {
        val listType = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(value, listType)
    }

    @TypeConverter
    fun fromEnumTipoMovimiento(tipo: EnumTipoMovimiento?): String? {
        return when (tipo) {
            EnumTipoMovimiento.VENTA -> "V"
            EnumTipoMovimiento.GASTO -> "G"
            null -> null
        }
    }

    @TypeConverter
    fun toEnumTipoMovimiento(value: String?): EnumTipoMovimiento? {
        return when (value?.uppercase()) {
            "V", "VENTA" -> EnumTipoMovimiento.VENTA
            "G", "GASTO" -> EnumTipoMovimiento.GASTO
            else -> null
        }
    }

    @TypeConverter
    fun fromMovimientoItemList(list: List<MovimientoItem>?): String? = gson.toJson(list)

    @TypeConverter
    fun toMovimientoItemList(value: String?): List<MovimientoItem>? {
        val listType = object : TypeToken<List<MovimientoItem>>() {}.type
        return gson.fromJson(value, listType)
    }
}
