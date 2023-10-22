package com.weatheriza.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class FavoriteCity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val countryCode: String,
    val latitude: Double,
    val longitude: Double
)