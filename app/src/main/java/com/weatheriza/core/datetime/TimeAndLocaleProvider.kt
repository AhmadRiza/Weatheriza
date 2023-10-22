package com.weatheriza.core.datetime

import kotlinx.datetime.TimeZone
import java.util.Locale

interface TimeAndLocaleProvider {
    val currentTimeMillis: Long
    val defaultTimezone: TimeZone
    val defaultLocale: Locale
}