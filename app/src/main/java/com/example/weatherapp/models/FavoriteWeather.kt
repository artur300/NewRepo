package com.example.weatherapp.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_weather")
data class FavoriteWeather(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val cityName: String,
    val temperature: Double,
    val description: String,
    val minTemp: Double,
    val maxTemp: Double,
    val feelsLike: Double,
    val windSpeed: Double,
    val iconCode: String
)
