package com.weatheriza.data.model

data class Forecast(
    val date: Long,
    val dateString: String,
    val temperature: Float,
    val feelsLike: Float,
    val humidity: Float,
    val windSpeed: Float,
    val weather: Weather,
    val city: City
)

data class Weather(
    val weatherType: WeatherType,
    val label: String,
    val description: String,
    val iconUrl: String
)

enum class WeatherType {
    THUNDERSTORM,
    DRIZZLE,
    RAIN,
    SNOW,
    ATMOSPHERE,
    CLOUD_N_CLEAR,
    UNKNOWN
}

data class City(
    val name: String,
    val country: String,
    val sunrise: Long,
    val sunset: Long,
    val latitude: Double,
    val longitude: Double
)