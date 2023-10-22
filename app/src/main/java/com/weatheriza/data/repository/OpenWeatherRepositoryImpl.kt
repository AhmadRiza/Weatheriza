package com.weatheriza.data.repository

import com.weatheriza.core.model.Result
import com.weatheriza.core.model.toResult
import com.weatheriza.data.local.OpenWeatherLocalDataSource
import com.weatheriza.data.mapper.toFiveDayForeCast
import com.weatheriza.data.mapper.toGeoLocation
import com.weatheriza.data.model.FiveDayForecast
import com.weatheriza.data.model.GeoLocation
import com.weatheriza.data.remote.OpenWeatherRemoteDataSource
import javax.inject.Inject

class OpenWeatherRepositoryImpl @Inject constructor(
    private val remoteDataSource: OpenWeatherRemoteDataSource,
    private val localDataSource: OpenWeatherLocalDataSource
) : OpenWeatherRepository {

    override suspend fun searchGeoLocation(query: String): Result<List<GeoLocation>> {
        return remoteDataSource.searchGeoLocation(query)
            .toResult { geoLocationEntities ->
                geoLocationEntities.map { it.toGeoLocation() }
            }
    }

    override suspend fun getWeatherForecast(
        latitude: Double,
        longitude: Double
    ): Result<FiveDayForecast> {
        return remoteDataSource.getFiveDaysForecast(latitude, longitude)
            .toResult { forecastEntity ->
                forecastEntity.toFiveDayForeCast()
            }
    }

    override var lastViewedLocation: GeoLocation
        get() = localDataSource.lastViewedLocation
        set(value) {
            localDataSource.lastViewedLocation = value
        }

    override suspend fun saveFavoriteCity(city: GeoLocation) {
        localDataSource.saveFavoriteCity(city)
    }

    override suspend fun deleteFavoriteCity(name: String) {
        localDataSource.deleteFavoriteCity(name)
    }

    override suspend fun getAllFavoriteCity(): List<GeoLocation> {
        return localDataSource.getAllFavoriteCity()
    }

    override suspend fun isCityFavorite(name: String): Boolean {
        return localDataSource.isFavorite(name)
    }

}