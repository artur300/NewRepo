package com.example.weatherapp.db

import android.content.Context
import com.example.weatherapp.models.Weather
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule{

    @Provides
    @Singleton
    fun provideWeatherDatabase(@ApplicationContext context: Context) : WeatherDatabase{
        return WeatherDatabase.getDataBase(context)
    }
}