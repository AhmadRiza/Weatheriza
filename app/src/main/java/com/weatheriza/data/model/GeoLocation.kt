package com.weatheriza.data.model

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class GeoLocation(
    val name: String,
    val countryCode: String,
    val latitude: Double,
    val longitude: Double
) : Parcelable