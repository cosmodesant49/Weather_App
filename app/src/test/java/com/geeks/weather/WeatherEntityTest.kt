package com.geeks.weather

import com.geeks.weather.db.WeatherEntity
import org.junit.Assert.assertEquals
import org.junit.Test

class WeatherEntityTest {

    @Test
    fun testWeatherEntityCreation() {
        val cityName = "New York"
        val temperature = 20.0
        val weatherEntity = WeatherEntity(cityName = cityName, temperature = temperature)

        assertEquals(cityName, weatherEntity.cityName)
        assertEquals(temperature, weatherEntity.temperature, 0.01)
    }
}
