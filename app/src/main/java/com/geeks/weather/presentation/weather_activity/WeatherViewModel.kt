package com.geeks.weather.presentation.weather_activity

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.geeks.weather.data.Resource
import com.geeks.weather.data.db.WeatherEntity
import com.geeks.weather.data.model.WeatherModel
import com.geeks.weather.domain.usecase.WeatherUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(private val weatherUseCase: WeatherUseCase) :
    ViewModel() {
    val weatherEntities: LiveData<List<WeatherEntity>> = liveData {
        emitSource(weatherUseCase.getDBWeater())
    }
    fun getWeather(city: String): LiveData<Resource<WeatherModel>> = weatherUseCase.getWeather(city)

    suspend fun insertWeather(weatherEntity: WeatherEntity) =
        weatherUseCase.insertWeather(weatherEntity)

  //  suspend fun getDBWeather(): LiveData<List<WeatherEntity>> = weatherUseCase.getDBWeater()
    suspend fun deleteWeather(weatherEntity: WeatherEntity) =
        weatherUseCase.deleteDBWeather(weatherEntity)
}


/* private val _weatherList = MutableLiveData<List<WeatherEntity>>()
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
*/