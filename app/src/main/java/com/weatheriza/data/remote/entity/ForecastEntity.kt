package com.weatheriza.data.remote.entity

import androidx.annotation.Keep

@Keep
data class ForecastEntity(
    val dt: Long?,
    val main: MainEntity?,
    val weather: WeatherEntity?,
    val wind: WindEntity?,
    val city: CityEntity?
) {
    @Keep
    data class MainEntity(
        val temp: Float?,
        val feelsLike: Float?,
        val tempMin: Float?,
        val tempMax: Float?,
        val humidity: Float?,
    )

    @Keep
    data class WeatherEntity(
        val id: Int?,
        val main: String?,
        val description: String?,
        val icon: String?
    )

    @Keep
    data class WindEntity(
        val speed: Float?,
        val degree: Float?,
    )

    @Keep
    data class CityEntity(
        val name: String?,
        val country: String?,
        val sunrise: Long?,
        val sunset: Long?,
        val coord: CoordinateEntity?
    ) {
        data class CoordinateEntity(
            val lat: Double?,
            val lon: Double?
        )
    }

}