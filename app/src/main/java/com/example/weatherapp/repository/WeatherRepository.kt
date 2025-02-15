package com.example.weatherapp.repository

import retrofit2.Response
import com.example.weatherapp.api.WeatherAPI
import com.example.weatherapp.db.WeatherDatabase
import com.example.weatherapp.models.CityResponse
import com.example.weatherapp.models.FavoriteWeather
import com.example.weatherapp.models.WeatherResponse
import javax.inject.Inject

class WeatherRepository @Inject constructor(
    private val api: WeatherAPI,
    private val db: WeatherDatabase
) {
    suspend fun getCoordinates(city: String, country: String? = null): Response<List<CityResponse>> {
        val query = if (country.isNullOrEmpty()) city else "$city,$country"
        return api.getCoordinates(query)
    }

    suspend fun getCitiesFromAPI(query: String): Response<List<CityResponse>> {
        return api.searchCities(query)
    }

    suspend fun getWeatherData(lat: Double, lon: Double, unit: String = "metric"): Response<WeatherResponse> {
        return api.getWeatherData(lat, lon, unit)
    }

    suspend fun insertWeatherData(weatherResponse: WeatherResponse) {
        db.getWeatherDao().upsertWeatherData(weatherResponse)
    }

    fun getSavedWeather() = db.getWeatherDao().getAllWeatherData()

    suspend fun deleteWeather(weatherResponse: WeatherResponse) {
        db.getWeatherDao().deleteWeatherData(weatherResponse)
    }

    // ------------------- ניהול מועדפים -------------------

    suspend fun getFavoriteWeatherList(): List<FavoriteWeather> {
        return db.getFavoriteDao().getAllFavoritesList() // פונקציה חדשה ב-DAO
    }


    suspend fun insertFavoriteWeather(favoriteWeather: FavoriteWeather) {
        db.getFavoriteDao().insertFavorite(favoriteWeather)
    }

    fun getFavoriteWeather() = db.getFavoriteDao().getAllFavorites()

    suspend fun deleteFavoriteWeather(favoriteWeather: FavoriteWeather) {
        db.getFavoriteDao().deleteFavorite(favoriteWeather)
    }

    /**
     * פונקציה שמעדכנת את רשימת המועדפים עם הנתונים העדכניים מה-API
     */
    suspend fun updateFavoriteWeather(favorites: List<FavoriteWeather>) {
        favorites.forEach { favorite ->
            val response = getWeatherData(favorite.lat, favorite.lon, "metric")
            if (response.isSuccessful) {
                response.body()?.let { weather ->
                    val updatedWeather = favorite.copy(  // מעדכן את הפריט הקיים ולא יוצר חדש
                        temperature = weather.main.temp,
                        description = weather.weather[0].description,
                        minTemp = weather.main.temp_min,
                        maxTemp = weather.main.temp_max,
                        feelsLike = weather.main.feels_like,
                        windSpeed = weather.wind.speed,
                        iconCode = weather.weather[0].icon
                    )
                    insertFavoriteWeather(updatedWeather)
                }
            }
        }
    }

}

