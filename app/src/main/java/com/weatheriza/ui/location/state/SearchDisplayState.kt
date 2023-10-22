package com.weatheriza.ui.location.state

sealed interface SearchDisplayState {
    data class DisplayLocationList(
        val geoLocationItems: List<GeoLocationItemModel>,
    ) : SearchDisplayState

    data object LocationNotFound : SearchDisplayState
    data object Empty : SearchDisplayState
    data class Error(val message: String) : SearchDisplayState
}