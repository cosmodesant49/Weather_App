package com.geeks.weather.data

sealed class Resource <T>(
    val message: String? = null,
    val data: T? = null
) {
    class Loading<T> : Resource<T>()
    class Success<T>(data: T) : Resource<T>(data = data)
    class Error<T>(message: String) : Resource<T>(message = message)
}