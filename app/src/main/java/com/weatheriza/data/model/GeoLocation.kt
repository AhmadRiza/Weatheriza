package com.weatheriza.data.model

data class GeoLocation(
    val name: String,
    val countryCode: String,
    val state: String,
    val latitude: Double,
    val longitude: Double
)