package com.geeks.weather.ui.weather_activity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.geeks.weather.data.db.WeatherDao
import com.geeks.weather.data.db.WeatherEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WeatherViewModel(private val weatherDao: WeatherDao) : ViewModel() {

    private val _weatherList = MutableLiveData<List<WeatherEntity>>()
    val weatherList: LiveData<List<WeatherEntity>> = _weatherList

    fun loadSavedCities() {
        viewModelScope.launch(Dispatchers.IO) {
            val savedCities = weatherDao.getAllWeather()
            withContext(Dispatchers.Main) {
                _weatherList.value = savedCities
            }
        }
    }

    fun deleteCity(weatherModel: WeatherEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            weatherDao.deleteWeather(weatherModel)
            loadSavedCities()
        }
    }

}
