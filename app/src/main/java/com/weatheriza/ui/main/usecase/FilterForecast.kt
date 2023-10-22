package com.weatheriza.ui.main.usecase

import com.weatheriza.core.base.NonSuspendingUseCase
import com.weatheriza.core.datetime.TimeAndLocaleProvider
import com.weatheriza.core.datetime.parseUnixDateTime
import com.weatheriza.data.model.Forecast
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import javax.inject.Inject
import kotlin.math.abs

class FilterForecast @Inject constructor(
    private val timeAndLocaleProvider: TimeAndLocaleProvider
) : NonSuspendingUseCase<List<Forecast>, List<Forecast>>() {
    companion object {
        private const val MAX_FORECAST_DISPLAYED = 3
        private const val ONE_DAY_HOURS = 24
    }

    override fun build(params: List<Forecast>?): List<Forecast> {
        requireNotNull(params)
        if (params.isEmpty()) throw IllegalStateException()

        val nowDateTime = Instant.fromEpochMilliseconds(timeAndLocaleProvider.currentTimeMillis)
            .toLocalDateTime(TimeZone.currentSystemDefault())
        val nowHour = nowDateTime.hour
        val nowDate = nowDateTime.date
        val next3Date = nowDate.plus(MAX_FORECAST_DISPLAYED, DateTimeUnit.DAY)

        val groupedForecast = params.groupBy {
            it.dateTime.date.dayOfMonth
        }.filter { it.key in nowDate.dayOfMonth..next3Date.dayOfMonth }

        return groupedForecast.map {
            it.value.closestWith(nowHour)
        }
    }

    private fun List<Forecast>.closestWith(hour: Int): Forecast {
        var closestItem: Forecast? = null
        var closestDiff = ONE_DAY_HOURS
        forEach {
            val diff = abs(it.dateTime.hour - hour)
            if (diff < closestDiff) {
                closestDiff = diff
                closestItem = it
            } else {
                return closestItem!!
            }
        }
        return closestItem!!
    }

    private val Forecast.dateTime: LocalDateTime
        get() = parseUnixDateTime(date, timeAndLocaleProvider.defaultTimezone)
}