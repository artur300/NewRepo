package com.example.weatherapp.ui

import android.Manifest
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.R
import com.example.weatherapp.models.WeatherResponse
import com.example.weatherapp.repository.WeatherRepository
import com.example.weatherapp.util.Resource
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val repository: WeatherRepository,
    application: Application
): AndroidViewModel(application){

    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(application)

    val weatherData : MutableLiveData<Resource<WeatherResponse>> = MutableLiveData()

    fun requestLocation(context: Context) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) !=
            PackageManager.PERMISSION_GRANTED) {
            weatherData.postValue(Resource.Error("Location permission not granted"))
            return
        }

        fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
            .addOnSuccessListener { location ->
                location?.let { getWeather(it.latitude, it.longitude) }
                    ?: weatherData.postValue(Resource.Error("Location unavailable"))
            }
            .addOnFailureListener { exception ->
                weatherData.postValue(Resource.Error("Failed to get location: ${exception.message}"))
            }
    }

    fun getWeather(lat: Double, lon: Double, unit: String = "metric"){
        weatherData.value = Resource.Loading()

        viewModelScope.launch {
            try {
                val response = repository.getWeatherData(lat, lon, unit)

                if(response.isSuccessful){
                    response.body()?.let {
                        weatherData.postValue(Resource.Success(it))
                    } ?: run{
                        weatherData.postValue(Resource.Error("No data found"))
                    }
                }else{
                    weatherData.postValue(Resource.Error(response.message()))
                }
            }catch (e: Exception){
                weatherData.postValue(Resource.Error("No data found"))
            }
        }
    }

    fun getWeatherIcon(iconCode: String?) : Int{
        val iconPrefix = iconCode?.substring(0,2)
       return when(iconPrefix){
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
}