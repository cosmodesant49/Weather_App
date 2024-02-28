package com.geeks.weather.model

import androidx.room.Entity

data class WeatherModel(
    var main: MainModel,
/*    var wind: WindModel,
    var clouds: CloudsModel,
    var sys: SysModel,*/
    var name: String,

)
data class MainModel(
    var temp: Double,
/*    var feels_like: Double,
    var temp_min: Double,
    var temp_max: Double,*/
)
/*class WindModel(
    var speed: Double
)
data class CloudsModel(
    var all: Int
)
data class SysModel(
    var country: String
)*/

