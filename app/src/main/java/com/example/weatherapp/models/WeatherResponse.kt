package com.example.weatherapp.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weatherInfo")
data class WeatherResponse(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val base: String,
    val clouds: Clouds,
    val cod: Int,
    val coord: Coord,
    val dt: Int,
    val main: Main,
    val name: String,
    val rain: Rain?,
    val sys: Sys,
    val timezone: Int,
    val visibility: Int,
    val weather: List<Weather>,
    val wind: Wind
)