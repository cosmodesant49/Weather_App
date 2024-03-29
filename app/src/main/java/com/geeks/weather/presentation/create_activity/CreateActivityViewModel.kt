package com.geeks.weather.presentation.create_activity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.geeks.weather.data.Resource
import com.geeks.weather.data.db.WeatherEntity
import com.geeks.weather.data.model.WeatherModel
import com.geeks.weather.domain.usecase.WeatherUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateActivityViewModel @Inject constructor(private val weatherUseCase: WeatherUseCase) :
    ViewModel() {

    fun getWeather(city: String): LiveData<Resource<WeatherModel>> = weatherUseCase.getWeather(city)

    suspend fun insertWeather(weatherEntity: WeatherEntity) = weatherUseCase.insertWeather(weatherEntity)



    }


