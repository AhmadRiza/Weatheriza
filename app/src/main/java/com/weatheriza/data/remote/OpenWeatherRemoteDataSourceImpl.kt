package com.weatheriza.data.remote

import com.weatheriza.core.entities.NetworkResult
import com.weatheriza.core.network.mapper.safeApiCall
import com.weatheriza.core.network.mapper.toNetworkResult
import com.weatheriza.data.remote.entity.ForecastEntity
import com.weatheriza.data.remote.entity.GeoLocationEntity
import com.weatheriza.data.remote.service.OpenWeatherService
import javax.inject.Inject

class OpenWeatherRemoteDataSourceImpl @Inject constructor(
    private val service: OpenWeatherService
) : OpenWeatherRemoteDataSource {
    override suspend fun searchGeoLocation(query: String): NetworkResult<List<GeoLocationEntity>> {
        return safeApiCall {
            service.searchGeoLocation(query).toNetworkResult()
        }
    }

    override suspend fun getFiveDaysForecast(
        latitude: Double,
        longitude: Double
    ): NetworkResult<List<ForecastEntity>> {
        return safeApiCall {
            service.getFiveDaysForeCast(
                latitude = latitude,
                longitude = longitude
            ).toNetworkResult()
        }
    }
}