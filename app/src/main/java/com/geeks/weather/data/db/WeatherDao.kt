package com.geeks.weather.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.geeks.weather.data.model.WeatherModel

@Dao
interface WeatherDao {

    @Query("SELECT * FROM weather")
    fun getAllWeather(): LiveData<List<WeatherEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertWeather(weather: WeatherEntity)

    @Delete
    fun deleteWeather(weatherEntities: WeatherEntity)

    @Query("DELETE FROM weather")
    fun deleteAllWeathers()

}
