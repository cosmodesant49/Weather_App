package com.geeks.weather.data

import com.geeks.weather.data.model.WeatherModel
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    @GET("data/2.5/weather")
    suspend fun getWeather(
        @Query("q") city: String,
        @Query("appid") key: String = "bdb2917eb8179d50d760b162dcdc2e24",
        @Query("units") unit: String = "metric"
    ): Response<WeatherModel>

}