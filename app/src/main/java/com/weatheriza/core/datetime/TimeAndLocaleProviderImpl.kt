package com.weatheriza.core.datetime

import kotlinx.datetime.TimeZone
import java.util.Locale
import javax.inject.Inject

class TimeAndLocaleProviderImpl @Inject constructor(

) : TimeAndLocaleProvider {
    override val currentTimeMillis: Long
        get() = System.currentTimeMillis()
    override val defaultTimezone: TimeZone
        get() = TimeZone.currentSystemDefault()
    override val defaultLocale: Locale
        get() = Locale.getDefault()
}