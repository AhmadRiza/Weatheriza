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

    var lastViewedLocation: GeoLocation
    suspend fun saveFavoriteCity(city: GeoLocation)
    suspend fun deleteFavoriteCity(name: String)
    suspend fun getAllFavoriteCity(): List<GeoLocation>
    suspend fun isFavorite(name: String): Boolean
}