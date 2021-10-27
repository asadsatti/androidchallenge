package com.example.androidchallenge.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.androidchallenge.R
import com.example.androidchallenge.databinding.ActivityWeatherBinding
import java.lang.IllegalArgumentException
import com.google.android.gms.location.*

class WeatherActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityWeatherBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(findViewById(R.id.toolbar_weather))

        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.fl_weather_container, WeatherFragment())
                .commitNow()
        }

        binding.bottomNavWeather.setOnNavigationItemSelectedListener {
            val fragment = when (it.itemId) {
                R.id.nav_weather -> {
                    WeatherFragment()

                }
                R.id.nav_weather_forecast -> {
                    WeatherForecastFragment()
                }
                else -> throw IllegalArgumentException("invalid menu choice")
            }

            supportFragmentManager
                .beginTransaction()
                .replace(R.id.fl_weather_container, fragment)
                .commitNow()

            return@setOnNavigationItemSelectedListener true
        }
    }
}