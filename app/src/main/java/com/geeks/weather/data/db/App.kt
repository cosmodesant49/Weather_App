package com.geeks.weather.data.db

import android.app.Application
import androidx.room.Room


class App : Application() {

    override fun onCreate() {
        super.onCreate()
        db = Room.databaseBuilder(
            applicationContext,
            WeatherDatabase::class.java, "database-name"
        ).allowMainThreadQueries().build()
    }

    companion object {
        lateinit var db: WeatherDatabase
    }
}