package com.example.androidchallenge.network.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class WeatherData(
    val current: Current,
    val daily: List<Daily>,
    val lat: Double,
    val lon: Double,
    val timezone: String,
    @SerializedName("timezone_offset") val timezoneOffset: Int
): Parcelable