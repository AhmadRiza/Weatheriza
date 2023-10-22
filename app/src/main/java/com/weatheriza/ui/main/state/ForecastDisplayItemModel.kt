package com.weatheriza.ui.main.state

data class ForecastDisplayItemModel(
    val dateUnix: String,
    val dayLabel: String,
    val dateLabel: String,
    val weatherIconUrl: String,
    val temperature: String,
    val isSelected: Boolean
)