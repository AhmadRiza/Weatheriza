package com.weatheriza.data.local

import android.content.SharedPreferences
import com.google.gson.Gson
import com.weatheriza.data.local.entity.FavoriteCity
import com.weatheriza.data.model.GeoLocation
import javax.inject.Inject

class OpenWeatherLocalDataSourceImpl @Inject constructor(
    private val favoriteCityDao: FavoriteCityDao,
    private val sharedPreferences: SharedPreferences
) : OpenWeatherLocalDataSource {

    companion object {
        const val KEY_LAST_LOCATION = "OpenWeatherLocalDataSourceImpl.KEY_LAST_LOCATION"
    }

    private val defaultLocation
        get() = GeoLocation(
            name = "Madiun",
            countryCode = "ID",
            latitude = -7.6290837,
            longitude = 111.5168819
        )

    override var lastViewedLocation: GeoLocation
        get() {
            val locationJson = sharedPreferences.getString(KEY_LAST_LOCATION, null).orEmpty()
            return if (locationJson.isEmpty()) {
                defaultLocation
            } else {
                Gson().fromJson(locationJson, GeoLocation::class.java)
            }
        }
        set(value) {
            val locationJson = Gson().toJson(value)
            sharedPreferences.edit().putString(
                KEY_LAST_LOCATION, locationJson
            ).apply()
        }

    override suspend fun saveFavoriteCity(city: GeoLocation) {
        val favoriteCity = FavoriteCity(
            name = city.name,
            countryCode = city.countryCode,
            latitude = city.latitude,
            longitude = city.longitude
        )
        favoriteCityDao.insertCity(favoriteCity)
    }

    override suspend fun deleteFavoriteCity(name: String) {
        favoriteCityDao.deleteCity(name)
    }

    override suspend fun getAllFavoriteCity(): List<GeoLocation> {
        return favoriteCityDao.getAllCities().map {
            GeoLocation(
                name = it.name,
                countryCode = it.countryCode,
                latitude = it.latitude,
                longitude = it.longitude
            )
        }
    }

    override suspend fun isFavorite(name: String): Boolean {
        return favoriteCityDao.getCityByName(name) != null
    }


}