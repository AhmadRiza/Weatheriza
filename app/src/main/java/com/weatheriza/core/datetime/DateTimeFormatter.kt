package com.weatheriza.core.datetime

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime


private const val ONE_MILLIS = 1000L
fun parseUnixDateTime(unixTime: Long, timezone: TimeZone): LocalDateTime {
    return Instant.fromEpochMilliseconds(unixTime * ONE_MILLIS)
        .toLocalDateTime(timezone)
}

fun LocalDateTime.displayDay(): String {
    return dayOfWeek.name.lowercase().replaceFirstChar { it.uppercase() }
}

fun LocalDateTime.displayDate(): String {
    val month = month.name.lowercase().replaceFirstChar { it.uppercase() }
    return "$dayOfMonth $month $year"
}