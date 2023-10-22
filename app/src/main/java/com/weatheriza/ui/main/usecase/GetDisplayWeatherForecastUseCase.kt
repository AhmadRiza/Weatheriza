package com.weatheriza.ui.main.usecase

import com.weatheriza.core.base.BaseUseCase
import com.weatheriza.core.datetime.TimeAndLocaleProvider
import com.weatheriza.core.datetime.displayDate
import com.weatheriza.core.datetime.displayDay
import com.weatheriza.core.datetime.parseUnixDateTime
import com.weatheriza.core.model.DefaultErrorMessage
import com.weatheriza.core.model.Result
import com.weatheriza.data.model.FiveDayForecast
import com.weatheriza.data.model.Forecast
import com.weatheriza.data.model.GeoLocation
import com.weatheriza.data.repository.OpenWeatherRepository
import com.weatheriza.ui.main.state.ForecastDisplayItemModel
import com.weatheriza.ui.main.state.MainDisplayState
import com.weatheriza.ui.main.state.WeatherDisplayModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetDisplayWeatherForecastUseCase @Inject constructor(
    private val repository: OpenWeatherRepository,
    private val filterForecast: FilterForecast,
    private val timeAndLocaleProvider: TimeAndLocaleProvider
) : BaseUseCase<Flow<GetDisplayWeatherForecastUseCase.Event>, GeoLocation>() {

    sealed interface Event {
        data class UpdateDisplayState(val displayState: MainDisplayState) : Event
        data class SaveCurrentForecast(val forecast: List<Forecast>) : Event
    }

    override suspend fun build(params: GeoLocation?): Flow<Event> {
        requireNotNull(params)
        return flow {
            emit(Event.UpdateDisplayState(MainDisplayState.Loading))
            when (val result = repository.getWeatherForecast(params.latitude, params.longitude)) {
                is Result.Error ->
                    emit(Event.UpdateDisplayState(MainDisplayState.Error(result.errorMessage)))

                Result.Success.Empty ->
                    emit(
                        Event.UpdateDisplayState(
                            MainDisplayState.Error(DefaultErrorMessage.UNKNOWN)
                        )
                    )

                is Result.Success.WithData -> {
                    proceedSuccessData(result.data)
                }
            }
        }
    }

    private suspend fun FlowCollector<Event>.proceedSuccessData(data: FiveDayForecast) {

        val filteredForeCast = filterForecast(data.forecasts)
        emit(Event.SaveCurrentForecast(filteredForeCast))

        val todayForecast = filteredForeCast.last()
        MainDisplayState.Success(
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
            forecasts = filteredForeCast.mapIndexed { index, forecast ->
                val dateTime = parseUnixDateTime(
                    forecast.date,
                    timeAndLocaleProvider.defaultTimezone
                )
                ForecastDisplayItemModel(
                    dateUnix = forecast.date.toString(),
                    dayLabel = if (index == 0) "Today" else dateTime.displayDay(),
                    dateLabel = dateTime.displayDate(),
                    weatherIconUrl = forecast.weather.iconUrl,
                    temperature = "${forecast.temperature}°C",
                    isSelected = index == 0
                )
            }
        ).let {
            emit(Event.UpdateDisplayState(it))
        }
    }

}