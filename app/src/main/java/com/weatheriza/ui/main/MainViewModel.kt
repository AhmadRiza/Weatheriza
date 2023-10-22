package com.weatheriza.ui.main

import androidx.lifecycle.viewModelScope
import com.weatheriza.core.base.BaseViewModel
import com.weatheriza.data.model.Forecast
import com.weatheriza.data.model.GeoLocation
import com.weatheriza.ui.main.state.ForecastDisplayItemModel
import com.weatheriza.ui.main.state.MainDisplayState
import com.weatheriza.ui.main.usecase.GetDisplayWeatherForecastUseCase
import com.weatheriza.ui.main.usecase.GetDisplayWeatherForecastUseCase.Event
import com.weatheriza.ui.main.usecase.GetWeatherDisplayModelUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getDisplayWeatherForecast: GetDisplayWeatherForecastUseCase,
    private val getWeatherDisplayModel: GetWeatherDisplayModelUseCase
) : BaseViewModel<MainViewModel.Intent, MainViewModel.State, MainViewModel.Effect>(State()) {

    data class State(
        val displayState: MainDisplayState = MainDisplayState.Loading
    )

    sealed interface Intent {
        data class OnViewCreated(val location: GeoLocation) : Intent
        data class OnForecastClick(val forecastModel: ForecastDisplayItemModel) : Intent
    }

    sealed interface Effect

    private var currentForecast: List<Forecast> = emptyList()
    private val mutex = Mutex()

    override fun onIntentReceived(intent: Intent) {
        when (intent) {
            is Intent.OnViewCreated -> onViewCreated(intent.location)
            is Intent.OnForecastClick -> {
                onOnForecastClick(intent.forecastModel)
            }
        }
    }

    private fun onViewCreated(geoLocation: GeoLocation) {
        viewModelScope.launch {
            getDisplayWeatherForecast(geoLocation)
                .flowOn(Dispatchers.IO)
                .collect { event ->
                    when (event) {
                        is Event.SaveCurrentForecast -> {
                            currentForecast = event.forecast
                        }

                        is Event.UpdateDisplayState -> {
                            setState { copy(displayState = event.displayState) }
                        }
                    }
                }
        }
    }

    private fun onOnForecastClick(selectedForecast: ForecastDisplayItemModel) {
        viewModelScope.launch {
            val display = (viewState.displayState as? MainDisplayState.Success) ?: return@launch
            mutex.withLock {
                val updatedForecasts = display.forecasts.toMutableList()
                if (selectedForecast.isSelected) {
                    return@withLock
                }
                val currentSelected = updatedForecasts.first { it.isSelected }
                val indexCurrent = updatedForecasts.indexOf(currentSelected)
                val indexSelected = updatedForecasts.indexOf(selectedForecast)
                if (indexCurrent != -1 && indexSelected != -1) {
                    updatedForecasts[indexCurrent] = currentSelected.copy(isSelected = false)
                    updatedForecasts[indexSelected] = selectedForecast.copy(isSelected = true)

                    val updatedDisplay = display.copy(
                        forecasts = updatedForecasts.toList()
                    )

                    setState { copy(displayState = updatedDisplay) }

                    val weatherDisplay = currentForecast
                        .first { it.date == selectedForecast.dateUnix }.let {
                            getWeatherDisplayModel(it)
                        }
                    setState {
                        copy(
                            displayState = updatedDisplay.copy(
                                displayedWeather = weatherDisplay
                            )
                        )
                    }
                }


            }
        }
    }


}