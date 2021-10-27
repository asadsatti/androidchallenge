package com.example.androidchallenge.repo

import com.example.androidchallenge.network.api.WeatherApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class WeatherRepo(private val weatherApi: WeatherApi) {
    suspend fun getWeatherForecast(lat: Double, lon: Double) = withContext(Dispatchers.IO) {
        weatherApi.getWeatherForecast(lat.toString(), lon.toString())
    }
}