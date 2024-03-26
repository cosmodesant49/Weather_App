package com.geeks.weather.domain.repository

import androidx.lifecycle.LiveData
import com.geeks.weather.data.Resource
import com.geeks.weather.data.db.WeatherEntity
import com.geeks.weather.data.model.WeatherModel

interface WeatherRepository {
    fun getWeather(city: String): LiveData<Resource<WeatherModel>>

    suspend fun getDBWeather(): LiveData<List<WeatherEntity>>

    suspend fun insertWeather(weatherEntity: WeatherEntity)

    suspend fun deleteWeather(weatherEntity: WeatherEntity)
}