package com.geeks.weather.data.retrofit

import com.geeks.weather.data.WeatherApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitService {

    private var retrofit = Retrofit.Builder().baseUrl("https://api.openweathermap.org/")
        .addConverterFactory(GsonConverterFactory.create()).build()

    var api = retrofit.create(WeatherApi::class.java)

}