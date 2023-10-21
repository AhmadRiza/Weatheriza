package com.weatheriza.data.remote

import com.weatheriza.data.remote.entity.ForecastEntity
import com.weatheriza.data.remote.entity.GeoLocationEntity
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenWeatherService {

    companion object {
        // standard, metric and imperial
        private const val DEFAULT_UNIT = "metric"
    }

    @GET("geo/1.0/direct")
    suspend fun searchGeoLocation(
        @Query("q") query: String
    ): Response<List<GeoLocationEntity>>

    @GET("data/2.5/forecast")
    suspend fun getFiveDaysForeCast(
        @Query("units") units: String = DEFAULT_UNIT,
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
    ): Response<List<ForecastEntity>>

}