package com.weatheriza.ui.main.usecase

import com.weatheriza.core.base.BaseUseCase
import com.weatheriza.core.datetime.TimeAndLocaleProvider
import com.weatheriza.core.datetime.displayDate
import com.weatheriza.core.datetime.displayDay
import com.weatheriza.core.datetime.parseUnixDateTime
import com.weatheriza.core.model.DefaultErrorMessage
import com.weatheriza.core.model.Result
import com.weatheriza.data.model.Forecast
import com.weatheriza.data.model.GeoLocation
import com.weatheriza.data.repository.OpenWeatherRepository
import com.weatheriza.ui.main.state.ForecastDisplayItemModel
import com.weatheriza.ui.main.state.MainDisplayState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetDisplayWeatherForecastUseCase @Inject constructor(
    private val repository: OpenWeatherRepository,
    private val filterForecast: FilterForecastUseCase,
    private val timeAndLocaleProvider: TimeAndLocaleProvider,
    private val getWeatherDisplay: GetWeatherDisplayModelUseCase
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
                    val filteredForeCast = filterForecast(result.data.forecasts)
                    emit(Event.SaveCurrentForecast(filteredForeCast))
                    val todayForecast = filteredForeCast.first()
                    MainDisplayState.Success(
                        cityLabel = "${result.data.city.name}, ${result.data.city.country}",
                        isCityFavorite = repository.isFavorite(params.name),
                        displayedWeather = getWeatherDisplay(todayForecast),
                        forecasts = filteredForeCast.toForecastDisplayItemModels()
                    ).let {
                        emit(Event.UpdateDisplayState(it))
                    }
                }
            }
            repository.lastViewedLocation = params
        }
    }

    private fun List<Forecast>.toForecastDisplayItemModels(): List<ForecastDisplayItemModel> {
        return mapIndexed { index, forecast ->
            val dateTime = parseUnixDateTime(
                forecast.date,
                timeAndLocaleProvider.defaultTimezone
            )
            ForecastDisplayItemModel(
                dateUnix = forecast.date,
                dayLabel = if (index == 0) "Today" else dateTime.displayDay(),
                dateLabel = dateTime.displayDate(),
                weatherIconUrl = forecast.weather.iconUrl,
                temperature = "${forecast.temperature}°C",
                isSelected = index == 0
            )
        }
    }

}