package com.weatheriza.data.remote

import com.weatheriza.core.network.mapper.toNetworkResult
import com.weatheriza.data.remote.entity.FiveDaysForecastEntity
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
            val mockForecastEntity = FiveDaysForecastEntity.ForecastEntity(
                dt = 1234,
                main = FiveDaysForecastEntity.ForecastEntity.MainEntity(
                    temp = 35.5f,
                    feelsLike = 40f,
                    tempMin = 20f,
                    tempMax = 40f,
                    humidity = 3f
                ),
                weather = listOf(
                    FiveDaysForecastEntity.ForecastEntity.WeatherEntity(
                        id = 200,
                        main = "Cloud",
                        description = "yes",
                        icon = "10d"
                    )
                ),
                wind = FiveDaysForecastEntity.ForecastEntity.WindEntity(speed = 10f, degree = 2f),
                dtTxt = "date"
            )
            val mockFiveDayForecast = FiveDaysForecastEntity(
                list = listOf(mockForecastEntity), city = FiveDaysForecastEntity.CityEntity(
                    name = "Jakarta",
                    country = "ID",
                    sunrise = 111,
                    sunset = 222,
                    coord = FiveDaysForecastEntity.CityEntity.CoordinateEntity(1.0, 2.0)
                )
            )

            coEvery {
                service.getFiveDaysForeCast(latitude = 11.0, longitude = 22.0)
            } returns Response.success(mockFiveDayForecast)
            should("should return correct network result") {
                dataSource.getFiveDaysForecast(11.0, 22.0) shouldBe
                        Response.success(mockFiveDayForecast).toNetworkResult()
            }
        }


    }
}
