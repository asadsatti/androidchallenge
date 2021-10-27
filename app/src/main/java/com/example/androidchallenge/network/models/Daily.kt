package com.example.androidchallenge.network.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Daily(
    val clouds: Int,
    @SerializedName("dew_point") val dewPoint: Double,
    val dt: Long,
    @SerializedName("feels_like") val feelsLike: FeelsLike,
    val humidity: Int,
    @SerializedName("moon_phase") val moonPhase: Double,
    val moonrise: Int,
    val moonset: Int,
    val pop: Double,
    val pressure: Int,
    val rain: Double,
    val sunrise: Int,
    val sunset: Int,
    val temp: Temp,
    val uvi: Double,
    val weather: List<Weather>,
    @SerializedName("wind_deg") val windDeg: Int,
    @SerializedName("wind_gust") val windGust: Double,
    @SerializedName("wind_speed") val windSpeed: Double
): Parcelable

@Parcelize
data class FeelsLike(
    val day: Double,
    val eve: Double,
    val morn: Double,
    val night: Double
): Parcelable

@Parcelize
data class Temp(
    val day: Double,
    val eve: Double,
    val max: Double,
    val min: Double,
    val morn: Double,
    val night: Double
): Parcelable