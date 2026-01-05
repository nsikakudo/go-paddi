package com.nig.gopaddi.core.util

import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Locale

object TripDateParser {
    private val inputFormatter = DateTimeFormatter.ofPattern("EEE MMM dd yyyy HH:mm:ss 'GMT'Z", Locale.ENGLISH)
    private val outputFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy", Locale.ENGLISH)

    fun formatRemoteDate(dateStr: String?): String {
        if (dateStr == null || dateStr == "Enter Date") return "Enter Date"

        return try {
            val cleanDate = dateStr.substringBefore(" (")
            val dateTime = ZonedDateTime.parse(cleanDate, inputFormatter)
            dateTime.format(outputFormatter)
        } catch (e: Exception) {
            dateStr
        }
    }

    fun calculateDuration(startStr: String?, endStr: String?): String {
        if (startStr == null || endStr == null) return "0 Days"
        return try {
            val cleanStart = startStr.substringBefore(" (")
            val cleanEnd = endStr.substringBefore(" (")
            val start = ZonedDateTime.parse(cleanStart, inputFormatter).toLocalDate()
            val end = ZonedDateTime.parse(cleanEnd, inputFormatter).toLocalDate()
            val days = kotlin.math.abs(ChronoUnit.DAYS.between(start, end))
            "$days Days"
        } catch (e: Exception) {
            "7 Days"
        }
    }
}