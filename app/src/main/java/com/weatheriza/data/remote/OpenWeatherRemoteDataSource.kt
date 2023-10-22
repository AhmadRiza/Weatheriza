package com.weatheriza.data.remote

import com.weatheriza.core.entities.NetworkResult
import com.weatheriza.data.remote.entity.FiveDaysForecastEntity
import com.weatheriza.data.remote.entity.GeoLocationEntity

interface OpenWeatherRemoteDataSource {
    suspend fun searchGeoLocation(query: String): NetworkResult<List<GeoLocationEntity>>
    suspend fun getFiveDaysForecast(
        latitude: Double,
        longitude: Double
    ): NetworkResult<FiveDaysForecastEntity>
}