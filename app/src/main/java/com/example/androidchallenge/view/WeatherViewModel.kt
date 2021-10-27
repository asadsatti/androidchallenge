package com.example.androidchallenge.view

import android.Manifest
import android.annotation.SuppressLint
import android.app.Application
import android.content.pm.PackageManager
import android.location.Geocoder
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.lifecycle.*
import com.example.androidchallenge.network.api.WeatherApi
import com.example.androidchallenge.network.models.Current
import com.example.androidchallenge.network.models.Daily
import com.example.androidchallenge.network.models.LocationModel
import com.example.androidchallenge.repo.WeatherRepo
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException


class WeatherViewModel(private val weatherRepo: WeatherRepo,
                       application: Application) : AndroidViewModel(application) {

    val requestLocationPermission: LiveData<Boolean>
        get() = _requestLocationPermission
    private val _requestLocationPermission = MutableLiveData<Boolean>()

    val location: LiveData<LocationModel>
        get() = _location
    private val _location = MutableLiveData<LocationModel>()

    val current: LiveData<Current>
    get() = _current
    private val _current = MutableLiveData<Current>()

    val daily: LiveData<List<Daily>>
    get() = _daily
    private val _daily = MutableLiveData<List<Daily>>()


    fun getWeatherForecast(lat: Double, lon: Double) {
        Log.d(WeatherViewModel::class.java.simpleName, ">>> getWeatherForecast")
        viewModelScope.launch {
            val weatherForecast = weatherRepo.getWeatherForecast(lat, lon)
            _current.value = weatherForecast.current
            _daily.value = weatherForecast.daily
        }
    }

    fun checkPermissionAndGetLocation() {
        if (notHaveLocationPermission()) {
            Log.d(WeatherViewModel::class.java.simpleName, ">>> don't have permission")
            _requestLocationPermission.value = true
        } else {
            Log.d(WeatherViewModel::class.java.simpleName, ">>> already have permission")
            _requestLocationPermission.value = false
            getLocation()
        }
    }

    private fun notHaveLocationPermission(): Boolean {

        return ActivityCompat.checkSelfPermission(
            getApplication<Application>().applicationContext, Manifest.permission.ACCESS_COARSE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED
                &&
                ActivityCompat.checkSelfPermission(
                    getApplication<Application>().applicationContext, Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
    }

    @SuppressLint("MissingPermission")
    fun getLocation() {
        Log.d(WeatherViewModel::class.java.simpleName, ">>> getLocation")
        val locationRequest = LocationRequest().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            interval = LOCATION_UPDATES_DESIRED_INTERVAL_IN_MILLI_SECONDS
            fastestInterval = LOCATION_UPDATES_FASTEST_INTERVAL_IN_MILLI_SECONDS
            smallestDisplacement = LOCATION_UPDATES_SMALLEST_DISPLACEMENT_IN_METERS
        }

        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                for (location in locationResult.locations) {
                    updateLocation(location.latitude, location.longitude)
                }
            }
        }

        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(getApplication<Application>().applicationContext)
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            null
        )
    }

    fun updateLocation(latitude: Double = DEFAULT_NEW_YORK_LATITUDE,
                       longitude: Double = DEFAULT_NEW_YORK_LONGITUDE) {
        val address = Geocoder(getApplication<Application>().applicationContext).getFromLocation(latitude, longitude, 1)
        val locationName = if (address.isNotEmpty()) {
            val add = address[0]
            "${add.countryCode}-${add.adminArea}-${add.locality}"
        } else {
            "Not found"
        }

        val newLocation = LocationModel(latitude, longitude, locationName)
        val oldLocation = _location.value
        if (oldLocation == null) {
            _location.value = newLocation
        } else {
            if (oldLocation != newLocation) {
                _location.value = newLocation
            }
        }
    }

    companion object {
        private const val DEFAULT_NEW_YORK_LATITUDE = 40.725302
        private const val DEFAULT_NEW_YORK_LONGITUDE = -73.997776

        // Desired interval for active location updates, in milliseconds.
        const val LOCATION_UPDATES_DESIRED_INTERVAL_IN_MILLI_SECONDS = 100L
        // Fastest interval for location updates, in milliseconds.
        const val LOCATION_UPDATES_FASTEST_INTERVAL_IN_MILLI_SECONDS = 100L
        // Minimum displacement between location updates in meters
        const val LOCATION_UPDATES_SMALLEST_DISPLACEMENT_IN_METERS = 5F
    }
}

class WeatherViewModelFactory(private val weatherApi: WeatherApi,
                              private val application: Application
                              ): ViewModelProvider.AndroidViewModelFactory(application) {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WeatherViewModel::class.java)) {
            return WeatherViewModel(WeatherRepo((weatherApi)), application) as T
        }
        throw IllegalArgumentException("Can not create ViewModel for class $modelClass")
    }
}

