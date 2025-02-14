package com.example.weatherapp.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.weatherapp.models.FavoriteWeather
import com.example.weatherapp.models.WeatherResponse

@Database(
    entities = [WeatherResponse::class, FavoriteWeather::class],
    version = 2,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class WeatherDatabase : RoomDatabase() {

    abstract fun getWeatherDao(): WeatherDao
    abstract fun getFavoriteDao(): FavoriteWeatherDao

    companion object {
        @Volatile
        private var instance: WeatherDatabase? = null

        fun getDataBase(context: Context): WeatherDatabase =
            instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    WeatherDatabase::class.java,
                    "weather_db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { instance = it }
            }
    }
}

