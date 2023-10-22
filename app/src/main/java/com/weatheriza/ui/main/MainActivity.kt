package com.weatheriza.ui.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import coil.load
import com.weatheriza.core.base.BaseVMActivity
import com.weatheriza.data.model.GeoLocation
import com.weatheriza.databinding.ActivityMainBinding
import com.weatheriza.databinding.ItemWeatherBinding
import com.weatheriza.ui.main.state.MainDisplayState
import com.weatheriza.ui.main.state.WeatherDisplayModel
import com.weatheriza.ui.main.usecase.ForecastAdapter
import com.weatheriza.ui.main.usecase.ForecastAdapterEvent
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity :
    BaseVMActivity<MainViewModel.Intent,
            MainViewModel.State,
            MainViewModel.Effect,
            MainViewModel>() {

    override val viewModel: MainViewModel by viewModels()

    private lateinit var binding: ActivityMainBinding
    private val forecastAdapter = initForecastAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupForecastList()

        dispatch(
            MainViewModel.Intent.OnViewCreated(
                location = GeoLocation(
                    name = "Jak",
                    countryCode = "Id",
                    latitude = -7.6290837,
                    longitude = 111.5168819
                )
            )
        )
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
                binding.layoutWeather.setState(display.displayedWeather)
                forecastAdapter.submitList(display.forecasts)
            }
        }
    }

    private fun ItemWeatherBinding.setState(model: WeatherDisplayModel) {
        textWeatherType.text = model.weatherLabel
        textCityName.text = model.cityLabel
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
                    MainViewModel.Intent.OnForecastClick(it.dateUnix)
                )
            }
        }
    }
}