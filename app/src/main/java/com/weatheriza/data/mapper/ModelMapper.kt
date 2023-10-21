package com.weatheriza.data.mapper

import com.weatheriza.core.network.HostUrl
import com.weatheriza.data.model.City
import com.weatheriza.data.model.Forecast
import com.weatheriza.data.model.GeoLocation
import com.weatheriza.data.model.Weather
import com.weatheriza.data.model.WeatherType
import com.weatheriza.data.remote.entity.ForecastEntity
import com.weatheriza.data.remote.entity.GeoLocationEntity

fun ForecastEntity.toForecast(): Forecast {
    return Forecast(
        date = dt ?: 0L,
        temperature = main?.temp ?: 0.0f,
        feelsLike = main?.feelsLike ?: 0.0f,
        humidity = main?.humidity ?: 0.0f,
        weather = Weather(
            weatherType = weather.getWeatherType(),
            label = weather?.main.orEmpty(),
            description = weather?.description.orEmpty(),
            iconUrl = HostUrl.OPEN_WEATHER_ICON_URL.format(weather?.icon.orEmpty())
        ),
        windSpeed = wind?.speed ?: 0.0f,
        city = City(
            name = city?.name.orEmpty(),
            country = city?.country.orEmpty(),
            sunset = city?.sunset ?: 0,
            sunrise = city?.sunrise ?: 0,
            latitude = city?.coord?.lat ?: 0.0,
            longitude = city?.coord?.lon ?: 0.0
        )

    )
}

fun ForecastEntity.WeatherEntity?.getWeatherType(): WeatherType {
    return when ((this?.id ?: 0) / 100) {
        2 -> WeatherType.THUNDERSTORM
        3 -> WeatherType.DRIZZLE
        5 -> WeatherType.RAIN
        6 -> WeatherType.SNOW
        7 -> WeatherType.ATMOSPHERE
        8 -> WeatherType.CLOUD_N_CLEAR
        else -> WeatherType.UNKNOWN
    }
}


fun GeoLocationEntity.toGeoLocation(): GeoLocation {
    return GeoLocation(
        name = name.orEmpty(),
        countryCode = countryCode.orEmpty(),
        latitude = lat ?: 0.0,
        longitude = lon ?: 0.0

    )
}