package com.weatheriza.ui.main.usecase

import com.weatheriza.core.base.BaseUseCase
import com.weatheriza.core.model.DefaultErrorMessage
import com.weatheriza.core.model.Result
import com.weatheriza.data.model.FiveDayForecast
import com.weatheriza.data.model.Forecast
import com.weatheriza.data.model.GeoLocation
import com.weatheriza.data.repository.OpenWeatherRepository
import com.weatheriza.ui.main.state.ForeCastDisplayItemModel
import com.weatheriza.ui.main.state.MainDisplayState
import com.weatheriza.ui.main.state.WeatherDisplayModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import javax.inject.Inject
import kotlin.math.abs

class GetDisplayWeatherForecast @Inject constructor(
    private val repository: OpenWeatherRepository
) : BaseUseCase<Flow<MainDisplayState>, GeoLocation>() {

    override suspend fun build(params: GeoLocation?): Flow<MainDisplayState> {
        requireNotNull(params)
        return flow {
            emit(MainDisplayState.Loading)
            when (val result = repository.getWeatherForecast(params.latitude, params.longitude)) {
                is Result.Error -> emit(MainDisplayState.Error(result.errorMessage))
                Result.Success.Empty ->
                    emit(MainDisplayState.Error(DefaultErrorMessage.UNKNOWN))

                is Result.Success.WithData -> {
                    emit(formatSuccessResult(result.data))
                }
            }
        }
    }

    private fun formatSuccessResult(data: FiveDayForecast):
            MainDisplayState.Success {
        val nowDateTime = Instant.fromEpochMilliseconds(System.currentTimeMillis())
            .toLocalDateTime(TimeZone.currentSystemDefault())
        val nowHour = nowDateTime.hour
        val nowDate = nowDateTime.date
        val next3Date = nowDate.plus(3, DateTimeUnit.DAY)

        val groupedForecast = data.forecasts.groupBy {
            it.dateTime.date.dayOfMonth
        }.filter { it.key in nowDate.dayOfMonth..next3Date.dayOfMonth }

        val closestTimeForecast = groupedForecast.map {
            it.value.closestWith(nowHour)
        }

        val todayForecast = closestTimeForecast.first()

        return MainDisplayState.Success(
            displayedWeather = WeatherDisplayModel(
                cityLabel = "${data.city.name}, ${data.city.country}",
                isCityFavorite = false,
                weatherLabel = todayForecast.weather.label,
                weatherIconUrl = todayForecast.weather.iconUrl,
                temperature = todayForecast.temperature.toInt().toString(),
                feelsLikeLabel = "Feels like ${todayForecast.feelsLike}°C",
                windSpeed = "${todayForecast.windSpeed} km/h",
                humidity = "${todayForecast.humidity}%",
                weatherType = todayForecast.weather.weatherType
            ),
            forecasts = closestTimeForecast.map { forecast ->
                ForeCastDisplayItemModel(
                    id = forecast.date.toString(),
                    dayLabel = forecast.dateTime.dayOfWeek.name.lowercase()
                        .replaceFirstChar { it.uppercase() },
                    dateLabel = forecast.dateTime.toString(),
                    weatherIconUrl = forecast.weather.iconUrl,
                    temperature = forecast.temperature.toString(),
                    isSelected = false
                )
            }
        )
    }

    private fun List<Forecast>.closestWith(hour: Int): Forecast {
        var closestItem: Forecast? = null
        var closestDiff = 24
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
        get() = Instant.fromEpochMilliseconds(date * 1000)
            .toLocalDateTime(TimeZone.currentSystemDefault())

}