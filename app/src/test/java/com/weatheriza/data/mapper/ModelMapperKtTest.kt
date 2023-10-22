package com.weatheriza.data.mapper

import com.weatheriza.data.model.City
import com.weatheriza.data.model.FiveDayForecast
import com.weatheriza.data.model.Forecast
import com.weatheriza.data.model.GeoLocation
import com.weatheriza.data.model.Weather
import com.weatheriza.data.model.WeatherType
import com.weatheriza.data.remote.entity.FiveDaysForecastEntity
import com.weatheriza.data.remote.entity.FiveDaysForecastEntity.CityEntity
import com.weatheriza.data.remote.entity.FiveDaysForecastEntity.ForecastEntity
import com.weatheriza.data.remote.entity.FiveDaysForecastEntity.ForecastEntity.WeatherEntity
import com.weatheriza.data.remote.entity.GeoLocationEntity
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe

class ModelMapperKtTest : ShouldSpec() {

    init {

        context("map weather entity weather type") {
            val mockWeatherEntity = WeatherEntity(
                id = 100, main = "Cloudy", description = "", icon = "10d"
            )
            should("should return correct type") {
                mockWeatherEntity.getWeatherType() shouldBe WeatherType.UNKNOWN
                mockWeatherEntity.copy(id = 200).getWeatherType() shouldBe WeatherType.THUNDERSTORM
                mockWeatherEntity.copy(id = 300).getWeatherType() shouldBe WeatherType.DRIZZLE
                mockWeatherEntity.copy(id = 500).getWeatherType() shouldBe WeatherType.RAIN
                mockWeatherEntity.copy(id = 600).getWeatherType() shouldBe WeatherType.SNOW
                mockWeatherEntity.copy(id = 700).getWeatherType() shouldBe WeatherType.ATMOSPHERE
                mockWeatherEntity.copy(id = 800).getWeatherType() shouldBe WeatherType.CLOUD_N_CLEAR

            }
        }

        context("map forecast entity to forecast") {
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
                    WeatherEntity(
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
                list = listOf(mockForecastEntity), city = CityEntity(
                    name = "Jakarta",
                    country = "ID",
                    sunrise = 111,
                    sunset = 222,
                    coord = CityEntity.CoordinateEntity(1.0, 2.0)
                )
            )
            should("should return correct model") {
                mockFiveDayForecast.toFiveDayForeCast() shouldBe FiveDayForecast(
                    forecasts = listOf(
                        Forecast(
                            date = 1234,
                            temperature = 35.5f,
                            feelsLike = 40f,
                            humidity = 3f,
                            windSpeed = 10f,
                            weather = Weather(
                                weatherType = WeatherType.THUNDERSTORM,
                                label = "Cloud",
                                description = "yes",
                                iconUrl = "https://openweathermap.org/img/wn/10d@4x.png"
                            ),
                            dateString = "date",
                        )
                    ),
                    city = City(
                        name = "Jakarta",
                        country = "ID",
                        sunrise = 111,
                        sunset = 222,
                        latitude = 1.0,
                        longitude = 2.0
                    )

                )


            }
        }

        context("geo location entity to geo location") {
            val mockGeoLocation = GeoLocationEntity(
                name = "Jakarta",
                country = "ID",
                lat = 11.0,
                lon = 22.00
            )
            should("should return correct model") {
                mockGeoLocation.toGeoLocation() shouldBe
                        GeoLocation(
                            name = "Jakarta",
                            countryCode = "ID",
                            latitude = 11.0,
                            longitude = 22.0
                        )
            }
        }
    }


}
