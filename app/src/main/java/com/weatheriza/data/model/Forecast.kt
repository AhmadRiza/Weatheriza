package com.weatheriza.data.model

data class Forecast(
    val date: Long,
    val temperature: Float,
    val humidity: Float,
    val weather: Weather,
    val city: City
)

data class Weather(
    val weatherType: WeatherType,
    val label: String,
    val description: String,
    val iconUrl: String
)

enum class WeatherType(val firstDigitId: Int) {
    THUNDERSTORM(2),
    DRIZZLE(3),
    RAIN(5),
    SNOW(6),
    ATMOSPHERE(7),
    CLOUD_N_CLEAR(8)
}

data class City(
    val name: String,
    val country: String,
    val sunset: Long,
    val sunrise: Long,
    val latitude: Double,
    val longitude: Double
)