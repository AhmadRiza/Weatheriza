package com.weatheriza.ui.location

import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import com.weatheriza.core.base.BaseVMActivity
import com.weatheriza.databinding.ActivitySearchBinding
import com.weatheriza.ui.location.state.SearchDisplayState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchLocationActivity : BaseVMActivity<SearchLocationViewModel.Intent,
        SearchLocationViewModel.State, SearchLocationViewModel.Effect, SearchLocationViewModel>() {

    override val viewModel: SearchLocationViewModel by viewModels()

    private lateinit var binding: ActivitySearchBinding

    private val geoLocationAdapter = initGeoLocationAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        configureViews()
        configureToolbar()
    }

    override fun renderEffect(effect: SearchLocationViewModel.Effect) {

    }

    override fun invalidate(state: SearchLocationViewModel.State) {
        when (val display = state.displayState) {
            is SearchDisplayState.DisplayLocationList -> {
                binding.layoutError.root.isVisible = false
                geoLocationAdapter.submitList(display.geoLocationItems)
            }

            SearchDisplayState.Empty -> {
                binding.layoutError.root.isVisible = false
                geoLocationAdapter.submitList(emptyList())
            }

            SearchDisplayState.LocationNotFound,
            is SearchDisplayState.Error -> {
                geoLocationAdapter.submitList(emptyList())
                binding.layoutError.root.isVisible = true
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
            dispatch(SearchLocationViewModel.Intent.OnQueryChanged(it?.toString().orEmpty()))
        }
        binding.layoutError.buttonChangeLocation.isVisible = false
    }

    private fun initGeoLocationAdapter(): GeoLocationAdapter {
        return GeoLocationAdapter {

        }
    }
}