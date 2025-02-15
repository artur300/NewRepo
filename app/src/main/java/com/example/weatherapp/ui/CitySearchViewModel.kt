package com.example.weatherapp.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.R
import com.example.weatherapp.models.WeatherResponse
import com.example.weatherapp.repository.WeatherRepository
import com.example.weatherapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import com.example.weatherapp.models.CityResponse
import javax.inject.Inject
import android.util.Log
import com.example.weatherapp.models.FavoriteWeather

@HiltViewModel
class CitySearchViewModel @Inject constructor(
    private val repository: WeatherRepository,
    private val app: Application
) : AndroidViewModel(app) {

    val weatherData = MutableLiveData<Resource<WeatherResponse>>()
    val cityList = MutableLiveData<List<CityResponse>>()
    val isRefreshing = MutableLiveData<Boolean>() // משתנה לעקוב אחרי מצב טעינת הרענון

    fun getWeatherByCity(city: String) {
        weatherData.value = Resource.Loading()

        viewModelScope.launch {
            try {
                val response = repository.getCoordinates(city)
                if (response.isSuccessful) {
                    val firstCity = response.body()?.firstOrNull()

                    firstCity?.let {
                        val weatherResponse = repository.getWeatherData(it.lat, it.lon, "metric")
                        handleWeatherResponse(weatherResponse)
                    } ?: weatherData.postValue(Resource.Error(app.getString(R.string.error_city_not_found)))

                } else {
                    weatherData.postValue(Resource.Error(app.getString(R.string.error_fetch_coordinates)))
                }
            } catch (e: Exception) {
                weatherData.postValue(Resource.Error(app.getString(R.string.error_fetch_weather)))
                Log.e("CitySearchViewModel", "Error fetching weather", e)
            }
        }
    }

    fun searchCities(query: String) {
        if (query.length < 2) return

        viewModelScope.launch {
            try {
                val response = repository.getCitiesFromAPI(query)
                if (response.isSuccessful && !response.body().isNullOrEmpty()) {
                    cityList.postValue(response.body())
                } else {
                    cityList.postValue(emptyList())
                }
            } catch (e: Exception) {
                cityList.postValue(emptyList())
                Log.e("CitySearchViewModel", "Error searching cities", e)
            }
        }
    }

    private fun handleWeatherResponse(response: retrofit2.Response<WeatherResponse>) {
        if (response.isSuccessful) {
            response.body()?.let {
                weatherData.postValue(Resource.Success(it))
            } ?: weatherData.postValue(Resource.Error(app.getString(R.string.error_no_data)))
        } else {
            weatherData.postValue(Resource.Error(app.getString(R.string.error_fetch_weather)))
        }
    }

    fun getWeatherIcon(iconCode: String?): Int {
        val iconPrefix = iconCode?.substring(0, 2)
        return when (iconPrefix) {
            "01" -> R.drawable.ic_sunny
            "02" -> R.drawable.ic_sunnycloudy
            "03" -> R.drawable.ic_cloudy
            "04" -> R.drawable.ic_very_cloudy
            "09" -> R.drawable.ic_rainshower
            "10" -> R.drawable.ic_rainy
            "11" -> R.drawable.ic_thunder
            "13" -> R.drawable.ic_snowy
            "50" -> R.drawable.ic_pressure
            else -> R.drawable.ic_launcher_foreground
        }
    }

    //-------------- ניהול רשימת המועדפים -----------------------

    val favoriteWeatherList = repository.getFavoriteWeather()

    fun saveWeatherToFavorites(weather: WeatherResponse) {
        viewModelScope.launch {
            val favoriteWeather = FavoriteWeather(
                cityName = weather.name,
                temperature = weather.main.temp,
                description = weather.weather[0].description,
                minTemp = weather.main.temp_min,
                maxTemp = weather.main.temp_max,
                feelsLike = weather.main.feels_like,
                windSpeed = weather.wind.speed,
                iconCode = weather.weather[0].icon,

                lat = weather.coord.lat,
                lon = weather.coord.lon

            )
            repository.insertFavoriteWeather(favoriteWeather)
        }
    }

    fun removeWeatherFromFavorites(favorite: FavoriteWeather) {
        viewModelScope.launch {
            repository.deleteFavoriteWeather(favorite)
        }
    }

    /**
     * פונקציה שמעדכנת את רשימת המועדפים עם הנתונים העדכניים ביותר מה-API
     */
    fun refreshFavoriteWeather(callback: (Boolean) -> Unit) {
        isRefreshing.postValue(true) // התחלת טעינה

        viewModelScope.launch {
            try {
                val updatedFavorites = favoriteWeatherList.value?.map { favorite ->
                    val response = repository.getWeatherData(favorite.lat, favorite.lon, "metric")

                    if (response.isSuccessful) {
                        response.body()?.let { weather ->
                            favorite.copy(  // מעדכן את הפריט הקיים במקום ליצור חדש
                                temperature = weather.main.temp,
                                description = weather.weather[0].description,
                                minTemp = weather.main.temp_min,
                                maxTemp = weather.main.temp_max,
                                feelsLike = weather.main.feels_like,
                                windSpeed = weather.wind.speed,
                                iconCode = weather.weather[0].icon
                            )
                        } ?: favorite
                    } else {
                        favorite // אם אין עדכון, שומר את הפריט הקיים
                    }
                } ?: emptyList()

                repository.updateFavoriteWeather(updatedFavorites) // שמור רק את הנתונים המעודכנים
                isRefreshing.postValue(false)
                callback(true)
            } catch (e: Exception) {
                isRefreshing.postValue(false)
                callback(false)
            }
        }
    }

}
