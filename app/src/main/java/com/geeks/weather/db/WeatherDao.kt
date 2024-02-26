package com.geeks.weather.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface WeatherDao {

    @Query("SELECT * FROM weather")
    fun getAllWeather(): List<WeatherEntity>



    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertWeather(weather: WeatherEntity)

    @Delete
    fun deleteAllWeather(vararg weatherEntities: WeatherEntity)

}
