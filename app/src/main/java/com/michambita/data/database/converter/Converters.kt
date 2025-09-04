package com.michambita.data.database.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.math.BigDecimal
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter
import java.util.Date

class Converters {

    private val gson = Gson()
    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

    // Date <-> Long (timestamp en millis)
    @TypeConverter
    fun fromDate(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun toDate(timestamp: Long?): Date? {
        return timestamp?.let { Date(it) }
    }

    // LocalDateTime <-> String
    @TypeConverter
    fun toLocalDateTime(value: String?): LocalDateTime? {
        return value?.let { LocalDateTime.parse(it, formatter) }
    }

    @TypeConverter
    fun fromLocalDateTime(dateTime: LocalDateTime?): String? {
        return dateTime?.format(formatter)
    }

    // BigDecimal <-> Double
    @TypeConverter
    fun fromBigDecimal(input: BigDecimal?): Double? {
        return input?.toDouble()
    }

    @TypeConverter
    fun toBigDecimal(input: Double?): BigDecimal? {
        return input?.let { BigDecimal.valueOf(it) }
    }

    // LocalDate <-> Long (epochDay para consistencia)
    @TypeConverter
    fun fromLong(value: Long?): LocalDate? {
        return value?.let { LocalDate.ofEpochDay(it) }
    }

    @TypeConverter
    fun toLong(date: LocalDate?): Long? {
        return date?.toEpochDay()
    }

    // List<String> <-> String
    @TypeConverter
    fun fromStringList(list: List<String>?): String? {
        return gson.toJson(list)
    }

    @TypeConverter
    fun toStringList(value: String?): List<String>? {
        val listType = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(value, listType)
    }
}
