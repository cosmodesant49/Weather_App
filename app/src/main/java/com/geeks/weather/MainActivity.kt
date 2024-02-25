package com.geeks.weather

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.geeks.weather.databinding.ActivityMainBinding
import com.geeks.weather.db.WeatherDao
import com.geeks.weather.db.WeatherDatabase
import com.geeks.weather.db.WeatherEntity
import com.geeks.weather.model.WeatherModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val list = mutableListOf<WeatherModel>()
    private lateinit var adapter: WeatherAdapter
    private lateinit var weatherDao: WeatherDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Инициализация базы данных и DAO Room
        val db = Room.databaseBuilder(applicationContext, WeatherDatabase::class.java, "weather-database").build()
        weatherDao = db.weatherDao()

        adapter = WeatherAdapter(list)
        binding.rvWeather.adapter = adapter

        initClickers()
        loadSavedCities()
    }

    private fun initClickers() {
        binding.ivAddCity.setOnClickListener {
            val intent = Intent(this, CreateActivity::class.java)
            startActivityForResult(intent, CREATE_ACTIVITY)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CREATE_ACTIVITY && resultCode == Activity.RESULT_OK) {
            data?.getStringExtra("city")?.let { city ->
                saveCity(city)
                loadData(city)
            }
        }
    }

    private fun saveCity(cityName: String) {
        val cityEntity = WeatherEntity(cityName = cityName,temperature = 0.0)
        GlobalScope.launch(Dispatchers.IO) {
            weatherDao.insertWeather(cityEntity)
        }
    }
    private fun loadSavedCities() {
        GlobalScope.launch(Dispatchers.IO) {
            val savedCities = weatherDao.getAllWeather()
            savedCities.forEach { city ->
                loadData(city.cityName)
            }
        }
    }

    private fun loadData(city: String) {
        RetrofitService().api.getWeather(city).enqueue(object : Callback<WeatherModel> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(call: Call<WeatherModel>, response: Response<WeatherModel>) {
                when {
                    response.isSuccessful -> {
                        response.body()?.let {
                            list.add(it)
                            adapter.notifyDataSetChanged()
                        }
                    }
                    else -> {
                        showToast("Ошибка: ${response.code()} - ${response.message()}")
                    }
                }
            }

            override fun onFailure(call: Call<WeatherModel>, t: Throwable) {
                showToast("Ошибка: ${t.localizedMessage}")
            }
        })
    }

    private fun showToast(message: String) {
        val toast = Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT)
        toast.show()
    }

    companion object {
        private const val CREATE_ACTIVITY = 1
    }
}
