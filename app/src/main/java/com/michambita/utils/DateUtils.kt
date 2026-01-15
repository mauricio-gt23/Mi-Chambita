package com.michambita.utils

import java.util.Calendar
import java.util.Date
import java.text.SimpleDateFormat
import java.util.Locale

object DateUtils {
    fun isToday(date: Date): Boolean {
        val today = Calendar.getInstance()
        val y = today.get(Calendar.YEAR)
        val m = today.get(Calendar.MONTH)
        val d = today.get(Calendar.DAY_OF_MONTH)
        val c = Calendar.getInstance()
        c.time = date
        return c.get(Calendar.YEAR) == y && c.get(Calendar.MONTH) == m && c.get(Calendar.DAY_OF_MONTH) == d
    }

    fun isSameDay(date1: Date, date2: Date): Boolean {
        val cal1 = Calendar.getInstance()
        cal1.time = date1
        val cal2 = Calendar.getInstance()
        cal2.time = date2
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH) &&
                cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH)
    }

    fun formatDate(date: Date): String {
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        return sdf.format(date)
    }
}