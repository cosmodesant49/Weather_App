package com.geeks.weather.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.geeks.weather.model.WeatherModel

@Database(entities = [WeatherEntity::class], version = 1)
abstract class WeatherDatabase : RoomDatabase() {

    abstract fun weatherDao(): WeatherDao
}
