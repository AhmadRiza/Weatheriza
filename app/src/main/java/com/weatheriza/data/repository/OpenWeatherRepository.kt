package com.weatheriza.data.repository

import com.weatheriza.core.model.Result
import com.weatheriza.data.model.FiveDayForecast
import com.weatheriza.data.model.GeoLocation

interface OpenWeatherRepository {
    suspend fun searchGeoLocation(query: String): Result<List<GeoLocation>>
    suspend fun getWeatherForecast(
        latitude: Double,
        longitude: Double
    ): Result<FiveDayForecast>

}