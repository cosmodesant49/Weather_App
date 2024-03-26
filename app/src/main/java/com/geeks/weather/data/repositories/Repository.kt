package com.geeks.weather.data.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import com.geeks.weather.base.BaseRepository
import com.geeks.weather.data.Resource
import com.geeks.weather.data.WeatherApi
import com.geeks.weather.data.db.WeatherDatabase
import com.geeks.weather.data.db.WeatherEntity
import com.geeks.weather.data.model.WeatherModel
import com.geeks.weather.domain.repository.WeatherRepository
import javax.inject.Inject

class Repository  @Inject constructor(private val api: WeatherApi, private val db: WeatherDatabase): BaseRepository(),WeatherRepository {

    override fun getWeather(city: String): LiveData<Resource<WeatherModel>> = apiRequest{
        api.getWeather(city).body()!!
    }

    override suspend fun getDBWeather(): LiveData<List<WeatherEntity>> = db.weatherDao().getAllWeather()
    override suspend fun insertWeather(weatherEntity: WeatherEntity) {
        Log.e("ololo", "$weatherEntity")
        db.weatherDao().insertWeather(weatherEntity)
    }
    override suspend fun deleteWeather(weatherEntity: WeatherEntity) = db.weatherDao().deleteWeather(weatherEntity)

}