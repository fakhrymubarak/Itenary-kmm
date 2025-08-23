package com.itenary.shared.util

object DateUtils {
    // Expects YYYY-MM-DD; performs a simple structural validation
    fun isValidDateIso(date: String): Boolean {
        if (date.length != 10) return false
        if (date[4] != '-' || date[7] != '-') return false
        val year = date.substring(0, 4)
        val month = date.substring(5, 7)
        val day = date.substring(8, 10)
        if (!year.all { it.isDigit() } || !month.all { it.isDigit() } || !day.all { it.isDigit() }) return false
        val m = month.toInt()
        val d = day.toInt()
        if (m !in 1..12) return false
        if (d !in 1..31) return false
        return true
    }

    // Safe string compare for dates in YYYY-MM-DD
    fun isStartBeforeOrEqualEnd(startDate: String, endDate: String): Boolean {
        return startDate <= endDate
    }
}


