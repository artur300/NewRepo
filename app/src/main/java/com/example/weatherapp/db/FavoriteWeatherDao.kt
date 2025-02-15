package com.example.weatherapp.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.weatherapp.models.FavoriteWeather

@Dao
interface FavoriteWeatherDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(favoriteWeather: FavoriteWeather)

    @Query("SELECT * FROM favorite_weather")
    fun getAllFavorites(): LiveData<List<FavoriteWeather>>

    @Query("SELECT * FROM favorite_weather")
    suspend fun getAllFavoritesList(): List<FavoriteWeather>

    @Delete
    suspend fun deleteFavorite(favoriteWeather: FavoriteWeather)
}
