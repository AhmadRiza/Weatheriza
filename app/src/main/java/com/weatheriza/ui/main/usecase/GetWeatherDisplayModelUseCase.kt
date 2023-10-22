package com.weatheriza.ui.main.usecase

import com.weatheriza.core.base.NonSuspendingUseCase
import com.weatheriza.data.model.Forecast
import com.weatheriza.ui.main.state.WeatherDisplayModel
import javax.inject.Inject

class GetWeatherDisplayModelUseCase @Inject constructor() :
    NonSuspendingUseCase<WeatherDisplayModel, Forecast>() {
    override fun build(params: Forecast?): WeatherDisplayModel {
        requireNotNull(params)
        return WeatherDisplayModel(
            weatherLabel = params.weather.label,
            weatherIconUrl = params.weather.iconUrl,
            temperature = params.temperature.toInt().toString(),
            feelsLikeLabel = "Feels like ${params.feelsLike}°C",
            windSpeed = "${params.windSpeed} km/h",
            humidity = "${params.humidity}%",
            weatherType = params.weather.weatherType
        )
    }
}