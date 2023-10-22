package com.weatheriza.ui.main.state

import com.weatheriza.data.model.WeatherType

data class WeatherDisplayModel(
    val weatherLabel: String,
    val weatherIconUrl: String,
    val temperature: String,
    val feelsLikeLabel: String,
    val windSpeed: String,
    val humidity: String,
    val weatherType: WeatherType
)
