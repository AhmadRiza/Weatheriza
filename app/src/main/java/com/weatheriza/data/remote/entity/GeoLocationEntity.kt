package com.weatheriza.data.remote.entity

import androidx.annotation.Keep

@Keep
data class GeoLocationEntity(
    val name: String?,
    val country: String?,
    val lat: Double?,
    val lon: Double?
)