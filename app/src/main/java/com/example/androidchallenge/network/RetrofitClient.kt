package com.example.androidchallenge.network

import com.example.androidchallenge.network.api.WeatherApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private const val BASE_URI_WEATHER = "https://api.openweathermap.org/"

    fun getWeatherApi(): WeatherApi {
        return getRetrofitBuilder()
            .baseUrl(BASE_URI_WEATHER)
            .build()
            .create(WeatherApi::class.java)

    }

    private fun getRetrofitBuilder(): Retrofit.Builder {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        val client = OkHttpClient.Builder().addInterceptor(logging).build()
        return Retrofit
            .Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
    }
}