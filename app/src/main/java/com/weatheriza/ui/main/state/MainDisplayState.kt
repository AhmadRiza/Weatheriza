package com.weatheriza.ui.main.state

sealed interface MainDisplayState {
    data object Loading : MainDisplayState
    data class Error(val message: String) : MainDisplayState
    data class Success(
        val displayedWeather: WeatherDisplayModel,
        val forecasts: List<ForeCastDisplayItemModel>
    ) : MainDisplayState

}