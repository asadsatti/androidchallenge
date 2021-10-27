package com.example.androidchallenge.common

import android.text.format.DateFormat
import java.util.*

object Util {
    private val calendar: Calendar = Calendar.getInstance()
    fun getDayDateFromSeconds(timeStampInSeconds:Long?):String{
        if(timeStampInSeconds == null) return ""
        calendar.timeInMillis = timeStampInSeconds * 1000
        return DateFormat.format("E, dd MMM yyyy", calendar).toString()
    }
}