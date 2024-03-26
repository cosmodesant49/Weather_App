package com.geeks.weather.domain.usecase

import androidx.lifecycle.LiveData
import com.geeks.weather.data.Resource
import com.geeks.weather.data.db.WeatherEntity
import com.geeks.weather.data.model.WeatherModel
import com.geeks.weather.domain.repository.WeatherRepository
import javax.inject.Inject

class WeatherUseCase @Inject constructor(private val repository: WeatherRepository){
    fun getWeather(city:String): LiveData<Resource<WeatherModel>> = repository.getWeather(city)

    suspend fun getDBWeater(): LiveData<List<WeatherEntity>> = repository.getDBWeather()
    suspend fun insertWeather(weatherEntity: WeatherEntity) = repository.insertWeather(weatherEntity)
    suspend fun deleteDBWeather (weatherEntity: WeatherEntity) = repository.deleteWeather(weatherEntity)
}