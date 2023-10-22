package com.weatheriza.ui.main

import androidx.lifecycle.viewModelScope
import com.weatheriza.core.base.BaseViewModel
import com.weatheriza.data.model.Forecast
import com.weatheriza.data.model.GeoLocation
import com.weatheriza.ui.main.state.MainDisplayState
import com.weatheriza.ui.main.usecase.GetDisplayWeatherForecastUseCase
import com.weatheriza.ui.main.usecase.GetDisplayWeatherForecastUseCase.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getDisplayWeatherForecast: GetDisplayWeatherForecastUseCase
) : BaseViewModel<MainViewModel.Intent, MainViewModel.State, MainViewModel.Effect>(State()) {

    data class State(
        val displayState: MainDisplayState = MainDisplayState.Loading
    )

    sealed interface Intent {
        data class OnViewCreated(val location: GeoLocation) : Intent
        data class OnForecastClick(val dateUnix: Long) : Intent
    }

    sealed interface Effect

    private var currentForecast: List<Forecast> = emptyList()
    private val mutex = Mutex()

    override fun onIntentReceived(intent: Intent) {
        when (intent) {
            is Intent.OnViewCreated -> onViewCreated(intent.location)
            is Intent.OnForecastClick -> {

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


}