package com.weatheriza.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class GeoLocation(
    val name: String,
    val countryCode: String,
    val latitude: Double,
    val longitude: Double
) : Parcelable