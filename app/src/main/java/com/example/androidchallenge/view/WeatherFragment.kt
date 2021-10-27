package com.example.androidchallenge.view

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import com.example.androidchallenge.R
import com.example.androidchallenge.common.ICON_BASE_URI
import com.example.androidchallenge.common.ICON_SIZE_DEFAULT
import com.example.androidchallenge.common.ICON_TYPE_PNG
import com.example.androidchallenge.common.REQUEST_CODE_LOCATION
import com.example.androidchallenge.databinding.FragmentWeatherBinding
import com.example.androidchallenge.network.RetrofitClient
import com.squareup.picasso.Picasso
import java.lang.StringBuilder

class WeatherFragment : Fragment() {

    private lateinit var binding: FragmentWeatherBinding
    private lateinit var weatherViewModel: WeatherViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        weatherViewModel = ViewModelProvider(requireActivity(), WeatherViewModelFactory(RetrofitClient.getWeatherApi(), requireActivity().application))
            .get(WeatherViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWeatherBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        weatherViewModel.requestLocationPermission.observe(viewLifecycleOwner, {
            if (it) {
                requestLocationPermissions()
            }
        })

        weatherViewModel.checkPermissionAndGetLocation()
        weatherViewModel.location?.observe(viewLifecycleOwner, {
            requireActivity().title = it.locationName
            weatherViewModel.getWeatherForecast(it.latitude, it.longitude)
        })

        weatherViewModel.current.observe(viewLifecycleOwner, { current ->
            binding.tvWeatherTemp.text = getString(R.string.weather_forecast_temp, current.temp)
            val (weather, icon) = if (current.weather.isNotEmpty()) {
                current.weather[0].run {
                    "$main - $description"
                } to current.weather[0].icon
            } else {
                "" to ""
            }

            val url = StringBuilder()
                .append(ICON_BASE_URI)
                .append(icon)
                .append(ICON_SIZE_DEFAULT)
                .append(ICON_TYPE_PNG).toString()
            Picasso.with(requireActivity())
                .load(url)
                .into(binding.ivWeatherIcon)

            binding.tvWeatherWeather.text = weather
            binding.tvWeatherFeelsLike.text = getString(R.string.weather_forecast_temp, current.feelsLike)
            binding.tvWeatherPressure.text = getString(R.string.weather_forecast_pressure, current.pressure)
            binding.tvWeatherWindSpeed.text = getString(R.string.weather_forecast_wind_speed, current.windSpeed)
            binding.tvWeatherWindDirection.text = getString(R.string.weather_forecast_wind_direction, current.windDeg)
            binding.tvWeatherHumidity.text = getString(R.string.weather_forecast_humidity, current.humidity)
        })
    }

    private fun requestLocationPermissions() {
        Log.d(WeatherFragment::class.java.simpleName, ">>> requestLocationPermissions")
        requestPermissions(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ),
            REQUEST_CODE_LOCATION
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        Log.d(WeatherFragment::class.java.simpleName, ">>> onRequestPermissionsResult")
        when (requestCode) {
            REQUEST_CODE_LOCATION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(
                            requireContext(),
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        Toast.makeText(requireContext(), R.string.weather_permission_granted, Toast.LENGTH_SHORT).show()
                        weatherViewModel.getLocation()
                    }
                } else {
                    Toast.makeText(requireContext(), R.string.weather_permission_denied, Toast.LENGTH_SHORT).show()
                    weatherViewModel.updateLocation()
                }
                return
            }
        }
    }
}