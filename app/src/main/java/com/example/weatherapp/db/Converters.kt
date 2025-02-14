package com.example.weatherapp.db

import androidx.room.TypeConverter
import com.example.weatherapp.models.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    private val gson = Gson()

    @TypeConverter
    fun fromClouds(clouds: Clouds?): String? {
        return gson.toJson(clouds)
    }

    @TypeConverter
    fun toClouds(data: String?): Clouds? {
        return gson.fromJson(data, object : TypeToken<Clouds?>() {}.type)
    }

    @TypeConverter
    fun fromCoord(coord: Coord?): String? {
        return gson.toJson(coord)
    }

    @TypeConverter
    fun toCoord(data: String?): Coord? {
        return gson.fromJson(data, object : TypeToken<Coord?>() {}.type)
    }

    @TypeConverter
    fun fromWind(wind: Wind?): String? {
        return gson.toJson(wind)
    }

    @TypeConverter
    fun toWind(data: String?): Wind? {
        return gson.fromJson(data, object : TypeToken<Wind?>() {}.type)
    }

    @TypeConverter
    fun fromWeatherList(weather: List<Weather>?): String? {
        return gson.toJson(weather)
    }

    @TypeConverter
    fun toWeatherList(data: String?): List<Weather>? {
        return gson.fromJson(data, object : TypeToken<List<Weather>?>() {}.type)
    }

    @TypeConverter
    fun fromMain(main: Main?): String? {
        return gson.toJson(main)
    }

    @TypeConverter
    fun toMain(data: String?): Main? {
        return gson.fromJson(data, object : TypeToken<Main?>() {}.type)
    }

    @TypeConverter
    fun fromRain(rain: Rain?): String? {
        return gson.toJson(rain)
    }

    @TypeConverter
    fun toRain(data: String?): Rain? {
        return gson.fromJson(data, object : TypeToken<Rain?>() {}.type)
    }

    @TypeConverter
    fun fromSys(sys: Sys?): String? {
        return gson.toJson(sys)
    }

    @TypeConverter
    fun toSys(data: String?): Sys? {
        return gson.fromJson(data, object : TypeToken<Sys?>() {}.type)
    }
}
