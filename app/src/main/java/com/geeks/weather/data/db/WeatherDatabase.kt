package com.geeks.weather.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.geeks.weather.data.model.WeatherModel

@Database(entities = [WeatherEntity::class], version = 1)
abstract class WeatherDatabase : RoomDatabase() {

    abstract fun weatherDao(): WeatherDao
}
