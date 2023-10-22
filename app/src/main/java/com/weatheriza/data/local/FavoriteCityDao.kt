package com.weatheriza.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.weatheriza.data.local.entity.FavoriteCity

@Dao
interface FavoriteCityDao {

    @Query("SELECT * FROM FavoriteCity")
    suspend fun getAllCities(): List<FavoriteCity>

    @Query("SELECT * FROM FavoriteCity WHERE name = :name LIMIT 1")
    suspend fun getCityByName(name: String): FavoriteCity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCity(city: FavoriteCity)

    @Query("DELETE FROM FavoriteCity WHERE name = :name")
    suspend fun deleteCity(name: String)

}