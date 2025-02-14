package com.example.weatherapp.util

import java.text.SimpleDateFormat
import java.util.*

fun convertUnixToTime(unixTime: Int?, timezone: Int?): String {
    if (unixTime == null || timezone == null) return "--"

    val date = Date((unixTime + timezone) * 1000L)
    val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
    return sdf.format(date)
}
