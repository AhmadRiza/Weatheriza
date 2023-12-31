package com.weatheriza.ui.main.state

sealed interface MainDisplayState {
    data object Loading : MainDisplayState
    data class Error(val message: String) : MainDisplayState
    data class Success(
        val cityLabel: String,
        val isCityFavorite: Boolean,
        val displayedWeather: WeatherDisplayModel,
        val forecasts: List<ForecastDisplayItemModel>
    ) : MainDisplayState

}