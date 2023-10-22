package com.weatheriza.ui.location

import androidx.lifecycle.viewModelScope
import com.weatheriza.core.base.BaseViewModel
import com.weatheriza.ui.location.SearchLocationViewModel.Effect
import com.weatheriza.ui.location.SearchLocationViewModel.Intent
import com.weatheriza.ui.location.SearchLocationViewModel.State
import com.weatheriza.ui.location.state.SearchDisplayState
import com.weatheriza.ui.location.usecase.SearchLocationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchLocationViewModel @Inject constructor(
    private val searchLocation: SearchLocationUseCase
) : BaseViewModel<Intent, State, Effect>(State()) {

    data class State(
        val displayState: SearchDisplayState = SearchDisplayState.Empty
    )

    sealed interface Intent {
        data object OnViewCreated : Intent
        data class OnQueryChanged(val query: String) : Intent
    }

    sealed interface Effect {
        data object ShowKeyBoard : Effect
    }

    private var searchJob: Job? = null

    override fun onIntentReceived(intent: Intent) {
        when (intent) {
            is Intent.OnQueryChanged -> onQueryChanged(intent.query)
            Intent.OnViewCreated -> {
                onQueryChanged("")
                setEffect(Effect.ShowKeyBoard)
            }
        }
    }

    private fun onQueryChanged(query: String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            searchLocation(query)
                .flowOn(Dispatchers.IO)
                .collect {
                    setState { copy(displayState = it) }
                }
        }
    }

}