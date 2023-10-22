package com.weatheriza.data.local

import com.weatheriza.data.model.GeoLocation

interface OpenWeatherLocalDataSource {

    var lastViewedLocation: GeoLocation
    suspend fun saveFavoriteCity(city: GeoLocation)
    suspend fun deleteFavoriteCity(name: String)
    suspend fun getAllFavoriteCity(): List<GeoLocation>
    suspend fun isFavorite(name: String): Boolean
}