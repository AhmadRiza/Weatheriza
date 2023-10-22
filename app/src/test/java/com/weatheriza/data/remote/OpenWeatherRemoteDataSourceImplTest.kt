package com.weatheriza.data.remote

import com.weatheriza.core.network.mapper.toNetworkResult
import com.weatheriza.data.remote.entity.ForecastEntity
import com.weatheriza.data.remote.entity.GeoLocationEntity
import com.weatheriza.data.remote.service.OpenWeatherService
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import retrofit2.Response

class OpenWeatherRemoteDataSourceImplTest : ShouldSpec() {
    private val service: OpenWeatherService = mockk()
    private val dataSource = OpenWeatherRemoteDataSourceImpl(service)

    init {

        context("searchGeoLocation") {
            val mockEntity = GeoLocationEntity(
                name = "Jkt",
                country = "ID",
                lat = 11.0,
                lon = 22.0
            )
            coEvery {
                service.searchGeoLocation("jkt")
            } returns Response.success(listOf(mockEntity))
            should("should return correct network result") {
                dataSource.searchGeoLocation("jkt") shouldBe
                        Response.success(listOf(mockEntity)).toNetworkResult()
            }
        }

        context("getFiveDaysForecast") {
            val mockForecastEntity = ForecastEntity(
                dt = 1234,
                main = ForecastEntity.MainEntity(
                    temp = 35.5f,
                    feelsLike = 40f,
                    tempMin = 20f,
                    tempMax = 40f,
                    humidity = 3f
                ),
                weather = ForecastEntity.WeatherEntity(
                    id = 200,
                    main = "Cloud",
                    description = "yes",
                    icon = "10d"
                ),
                wind = ForecastEntity.WindEntity(speed = 10f, degree = 2f),
                city = ForecastEntity.CityEntity(
                    name = "Jakarta",
                    country = "ID",
                    sunrise = 111,
                    sunset = 222,
                    coord = ForecastEntity.CityEntity.CoordinateEntity(1.0, 2.0)
                )
            )

            coEvery {
                service.getFiveDaysForeCast(latitude = 11.0, longitude = 22.0)
            } returns Response.success(listOf(mockForecastEntity))
            should("should return correct network result") {
                dataSource.getFiveDaysForecast(11.0, 22.0) shouldBe
                        Response.success(listOf(mockForecastEntity)).toNetworkResult()
            }
        }


    }
}
