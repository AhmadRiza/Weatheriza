package com.weatheriza.ui.main

import android.os.Bundle
import android.util.Log
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
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity :
    BaseVMActivity<MainViewModel.Intent,
            MainViewModel.State,
            MainViewModel.Effect,
            MainViewModel>() {

    override val viewModel: MainViewModel by viewModels()

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
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
        Log.e("state", state.toString())
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
        Log.e("load from", model.weatherIconUrl)
        imageIcon.load(model.weatherIconUrl)
    }
}