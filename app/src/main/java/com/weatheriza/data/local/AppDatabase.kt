package com.weatheriza.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.weatheriza.data.local.entity.FavoriteCity

@Database(entities = [FavoriteCity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteCityDao(): FavoriteCityDao
}