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

    fun formatDate(date: Date): String {
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        return sdf.format(date)
    }
}