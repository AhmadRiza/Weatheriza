package com.weatheriza.ui.main

import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import coil.load
import com.weatheriza.R
import com.weatheriza.core.base.BaseVMActivity
import com.weatheriza.core.utils.parcelable
import com.weatheriza.data.model.GeoLocation
import com.weatheriza.databinding.ActivityMainBinding
import com.weatheriza.databinding.ItemWeatherBinding
import com.weatheriza.ui.location.SearchLocationActivity
import com.weatheriza.ui.main.state.MainDisplayState
import com.weatheriza.ui.main.state.WeatherDisplayModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity :
    BaseVMActivity<MainViewModel.Intent,
            MainViewModel.State,
            MainViewModel.Effect,
            MainViewModel>() {

    companion object {
        const val EXTRA_KEY_GEOLOCATION = "EXTRA_KEY_GEOLOCATION"
    }

    override val viewModel: MainViewModel by viewModels()

    private lateinit var binding: ActivityMainBinding
    private val forecastAdapter = initForecastAdapter()

    private val searchActivityLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            result.data?.parcelable<GeoLocation>(EXTRA_KEY_GEOLOCATION)?.let {
                dispatch(MainViewModel.Intent.OnGeoLocationReceived(it))
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupForecastList()
        window.statusBarColor = ContextCompat.getColor(this, R.color.md_blue_200)

        dispatch(MainViewModel.Intent.OnViewCreated)
    }

    private fun setupView() {
        binding.layoutWeather.run {
            buttonSearch.setOnClickListener {
                searchActivityLauncher.launch(
                    SearchLocationActivity.createIntent(this@MainActivity)
                )
            }
        }
    }

    override fun renderEffect(effect: MainViewModel.Effect) {

    }

    override fun invalidate(state: MainViewModel.State) {
        when (val display = state.displayState) {
            is MainDisplayState.Error -> {
                binding.swipeRefresh.isInvisible = true
                binding.layoutError.root.isVisible = true
                binding.layoutError.textErrorMessage.text = display.message
            }

            MainDisplayState.Loading -> {
                binding.swipeRefresh.isVisible = true
                binding.layoutContent.isInvisible = true
                binding.swipeRefresh.isRefreshing = true
            }

            is MainDisplayState.Success -> {
                binding.swipeRefresh.isVisible = true
                binding.layoutContent.isInvisible = false
                binding.swipeRefresh.isRefreshing = false
                binding.layoutWeather.setState(
                    display.displayedWeather,
                    cityLabel = display.cityLabel,
                    isCityFavorite = display.isCityFavorite
                )
                forecastAdapter.submitList(display.forecasts)
            }
        }
    }

    private fun ItemWeatherBinding.setState(
        model: WeatherDisplayModel,
        cityLabel: String,
        isCityFavorite: Boolean
    ) {
        textWeatherType.text = model.weatherLabel
        textCityName.text = cityLabel

        val colorFavorite = if (isCityFavorite) R.color.md_pink_500 else R.color.smoke
        buttonFavorite.setIconTintResource(colorFavorite)

        textWindSpeed.text = model.windSpeed
        textTemperature.text = model.temperature
        textFeelsLike.text = model.feelsLikeLabel
        textHumidity.text = model.humidity
        imageIcon.load(model.weatherIconUrl)
    }


    private fun setupForecastList() {
        binding.recyclerViewForecast.run {
            adapter = forecastAdapter
        }
    }

    private fun initForecastAdapter(): ForecastAdapter {
        return ForecastAdapter {
            when (it) {
                is ForecastAdapterEvent.OnForecastClick -> dispatch(
                    MainViewModel.Intent.OnForecastClick(it.model)
                )
            }
        }
    }
}