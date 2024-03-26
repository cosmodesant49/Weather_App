package com.geeks.weather.ui.create_activity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class CreateActivityViewModel : ViewModel() {

    private val _searchResult = MutableLiveData<Result<String>>()
    val searchResult: LiveData<Result<String>> = _searchResult

    fun searchCity(cityName: String) {
        viewModelScope.launch {
            // Simulate network delay
            delay(1000)

            // Simulate a search operation with validation
            if (cityName.isBlank()) {
                _searchResult.postValue(Result.failure(IllegalArgumentException("City name cannot be blank")))
            } else {
                // For demonstration, the search is always successful if there's input.
                _searchResult.postValue(Result.success(cityName))
            }
        }
    }
}
