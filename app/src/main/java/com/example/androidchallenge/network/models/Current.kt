package com.example.androidchallenge.network.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Current(
    val clouds: Int,
    @SerializedName("dew_point") val dewPoint: Double,
    val dt: Long,
    @SerializedName("feels_like") val feelsLike: Double,
    val humidity: Int,
    val pressure: Int,
    val sunrise: Int,
    val sunset: Int,
    val temp: Double,
    val uvi: Double,
    val visibility: Int,
    val weather: List<Weather>,
    @SerializedName("wind_deg") val windDeg: Int,
    @SerializedName("wind_gust") val windGust: Double,
    @SerializedName("wind_speed") val windSpeed: Double
): Parcelable

@Parcelize
data class Weather(
    val description: String,
    val icon: String,
    val id: Int,
    val main: String
): Parcelable