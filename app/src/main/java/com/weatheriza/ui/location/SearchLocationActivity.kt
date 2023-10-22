package com.weatheriza.ui.location

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import com.weatheriza.core.base.BaseVMActivity
import com.weatheriza.core.utils.showKeyboard
import com.weatheriza.databinding.ActivitySearchBinding
import com.weatheriza.ui.location.SearchLocationViewModel.Intent.OnQueryChanged
import com.weatheriza.ui.location.state.SearchDisplayState
import com.weatheriza.ui.main.MainActivity.Companion.EXTRA_KEY_GEOLOCATION
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchLocationActivity : BaseVMActivity<SearchLocationViewModel.Intent,
        SearchLocationViewModel.State, SearchLocationViewModel.Effect, SearchLocationViewModel>() {

    companion object {
        fun createIntent(context: Context) = Intent(context, SearchLocationActivity::class.java)

    }

    override val viewModel: SearchLocationViewModel by viewModels()

    private lateinit var binding: ActivitySearchBinding

    private val geoLocationAdapter = initGeoLocationAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        configureViews()
        configureToolbar()
        dispatch(SearchLocationViewModel.Intent.OnViewCreated)
    }

    override fun renderEffect(effect: SearchLocationViewModel.Effect) {
        when (effect) {
            SearchLocationViewModel.Effect.ShowKeyBoard -> {
                binding.editTextSearch.requestFocus()
                showKeyboard()
            }
        }
    }

    override fun invalidate(state: SearchLocationViewModel.State) {
        when (val display = state.displayState) {
            is SearchDisplayState.DisplayLocationList -> {
                binding.layoutError.root.isVisible = false
                geoLocationAdapter.submitList(display.geoLocationItems)
            }

            SearchDisplayState.LocationNotFound,
            SearchDisplayState.Empty -> {
                binding.layoutError.root.isVisible = false
                geoLocationAdapter.submitList(emptyList())
            }

            is SearchDisplayState.Error -> {
                geoLocationAdapter.submitList(emptyList())
                binding.layoutError.root.isVisible = true
                binding.layoutError.textErrorMessage.text = display.message
            }
        }
    }

    private fun configureToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun configureViews() {
        binding.recyclerViewLocations.adapter = geoLocationAdapter

        binding.editTextSearch.doAfterTextChanged {
            dispatch(OnQueryChanged(it?.toString().orEmpty()))
        }
        binding.layoutError.buttonChangeLocation.isVisible = false
        binding.layoutError.buttonTryAgain.setOnClickListener {
            dispatch(OnQueryChanged(binding.editTextSearch.text.toString()))
        }
    }

    private fun initGeoLocationAdapter(): GeoLocationAdapter {
        return GeoLocationAdapter {
            when (it) {
                is GeoLocationAdapterEvent.OnGeoLocationItemClicked -> {
                    setResult(
                        RESULT_OK,
                        Intent().apply { putExtra(EXTRA_KEY_GEOLOCATION, it.geoLocation) }
                    )
                    finish()
                }
            }
        }
    }
}