package com.weatheriza.data.remote

import retrofit2.http.GET

interface OpenWeatherService {

    @GET("")
    fun searchLocation(keyWord: String)

}