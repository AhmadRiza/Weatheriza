package com.weatheriza.data.repository

import com.weatheriza.core.model.Result
import com.weatheriza.core.model.toResult
import com.weatheriza.data.mapper.toFiveDayForeCast
import com.weatheriza.data.mapper.toGeoLocation
import com.weatheriza.data.model.FiveDayForecast
import com.weatheriza.data.model.GeoLocation
import com.weatheriza.data.remote.OpenWeatherRemoteDataSource
import javax.inject.Inject

class OpenWeatherRepositoryImpl @Inject constructor(
    private val weatherRemoteDataSource: OpenWeatherRemoteDataSource
) : OpenWeatherRepository {

    override suspend fun searchGeoLocation(query: String): Result<List<GeoLocation>> {
        return weatherRemoteDataSource.searchGeoLocation(query)
            .toResult { geoLocationEntities ->
                geoLocationEntities.map { it.toGeoLocation() }
            }
    }

    override suspend fun getWeatherForecast(
        latitude: Double,
        longitude: Double
    ): Result<FiveDayForecast> {
        return weatherRemoteDataSource.getFiveDaysForecast(latitude, longitude)
            .toResult { forecastEntity ->
                forecastEntity.toFiveDayForeCast()
            }
    }

}