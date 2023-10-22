package com.weatheriza.data.repository

import com.weatheriza.core.entities.NetworkResult
import com.weatheriza.core.model.Result
import com.weatheriza.data.local.OpenWeatherLocalDataSource
import com.weatheriza.data.mapper.toFiveDayForeCast
import com.weatheriza.data.mapper.toGeoLocation
import com.weatheriza.data.remote.OpenWeatherRemoteDataSource
import com.weatheriza.data.remote.entity.FiveDaysForecastEntity
import com.weatheriza.data.remote.entity.FiveDaysForecastEntity.ForecastEntity
import com.weatheriza.data.remote.entity.GeoLocationEntity
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.mockk

class OpenWeatherRepositoryImplTest : ShouldSpec() {

    private val remoteDataSource = mockk<OpenWeatherRemoteDataSource>()
    private val localDataSource = mockk<OpenWeatherLocalDataSource>()
    private val repository = OpenWeatherRepositoryImpl(
        remoteDataSource = remoteDataSource,
        localDataSource = localDataSource

    )

    init {
        beforeTest { clearAllMocks() }

        context("searchGeoLocation") {

            val mockEntity = GeoLocationEntity(
                name = "Jkt",
                country = "ID",
                lat = 11.0,
                lon = 22.0
            )
            beforeTest {
                coEvery {
                    remoteDataSource.searchGeoLocation("Jkt")
                } returns NetworkResult.Success.WithData(
                    listOf(
                        mockEntity
                    )
                )
            }

            should(" should return correct result") {
                repository.searchGeoLocation("Jkt") shouldBe
                        Result.Success.WithData(listOf(mockEntity.toGeoLocation()))
            }
        }

        context("getWeatherForecast") {
            val mockForecastEntity = ForecastEntity(
                dt = 1234,
                main = ForecastEntity.MainEntity(
                    temp = 35.5f,
                    feelsLike = 40f,
                    tempMin = 20f,
                    tempMax = 40f,
                    humidity = 3f
                ),
                weather = listOf(
                    ForecastEntity.WeatherEntity(
                        id = 200,
                        main = "Cloud",
                        description = "yes",
                        icon = "10d"
                    )
                ),
                wind = ForecastEntity.WindEntity(speed = 10f, degree = 2f),
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

            beforeTest {
                coEvery {
                    remoteDataSource.getFiveDaysForecast(11.0, 22.0)
                } returns NetworkResult.Success.WithData(
                    mockFiveDayForecast
                )
            }

            should(" should return correct result") {
                repository.getWeatherForecast(11.0, 22.0) shouldBe
                        Result.Success.WithData(mockFiveDayForecast.toFiveDayForeCast())
            }
        }

    }


}