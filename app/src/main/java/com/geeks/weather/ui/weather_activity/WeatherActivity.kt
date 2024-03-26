package com.geeks.weather.ui.weather_activity

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.geeks.weather.data.retrofit.RetrofitService
import com.geeks.weather.databinding.ActivityMainBinding
import com.geeks.weather.data.db.App
import com.geeks.weather.data.db.WeatherDao
import com.geeks.weather.data.db.WeatherDatabase
import com.geeks.weather.data.db.WeatherEntity
import com.geeks.weather.data.model.WeatherModel
import com.geeks.weather.ui.create_activity.CreateActivity
import com.geeks.weather.ui.weather_activity.adapter.WeatherAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WeatherActivity : AppCompatActivity() {

    //private lateinit var retrofitService: RetrofitService
    private lateinit var binding: ActivityMainBinding
    private lateinit var weatherDao: WeatherDao
    private val list = mutableListOf<WeatherEntity>()
    private val adapter = WeatherAdapter(list) { weatherModel, position ->
        onClickItem(weatherModel, position)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val db = Room.databaseBuilder(
            applicationContext,
            WeatherDatabase::class.java,
            "weather-database"
        ).build()

        weatherDao = db.weatherDao()

        binding.rvWeather.adapter = adapter

        initClickers()
        loadSavedCities()
        adapter.notifyDataSetChanged()
    }

    private fun initClickers() {
        binding.ivAddCity.setOnClickListener {
            val intent = Intent(this, CreateActivity::class.java)
            startActivityForResult(intent, CREATE_ACTIVITY)
        }
    }

    private fun onClickItem(weatherModel: WeatherEntity, position: Int) {
        showAlertDialog(weatherModel)
    }

    private fun showAlertDialog(weatherModel: WeatherEntity) {
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle(weatherModel.cityName)
            .setMessage("Are you sure you want to delete this city?")
            .setCancelable(true)
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton("Delete") { _, _ ->
                deleteCity(weatherModel)
            }
            .show()
    }

    private fun deleteCity(weatherModel: WeatherEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            weatherDao.deleteWeather(weatherModel)
            loadSavedCities()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CREATE_ACTIVITY && resultCode == Activity.RESULT_OK) {
            data?.getStringExtra("city")?.let { city ->
                loadData(city)
                adapter.notifyDataSetChanged()
            }
        }
    }

/*    private fun saveCity(cityName: String) {
        val cityEntity = WeatherEntity(cityName = cityName, temperature = 0.0)
        CoroutineScope(Dispatchers.IO).launch {
            weatherDao.insertWeather(cityEntity)
        }
    }*/

    private fun loadSavedCities() {
        CoroutineScope(Dispatchers.IO).launch {
            val savedCities = weatherDao.getAllWeather()
            withContext(Dispatchers.Main) {
                list.clear()
                list.addAll(savedCities)
                adapter.notifyDataSetChanged()
            }
        }
    }

    private fun loadData(city: String) {
        RetrofitService().api.getWeather(city).enqueue(object : Callback<WeatherModel> {
            override fun onResponse(call: Call<WeatherModel>, response: Response<WeatherModel>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        val weatherEntity = WeatherEntity(cityName = it.name, temperature = it.main.temp)
                        CoroutineScope(Dispatchers.IO).launch {
                            weatherDao.insertWeather(weatherEntity)
                            loadSavedCities()
                        }
                    }
                } else {
                    showToast("Error: ${response.code()} - ${response.message()}")
                }
            }

            override fun onFailure(call: Call<WeatherModel>, t: Throwable) {
                showToast("Error: ${t.localizedMessage}")
            }
        })
    }

    private fun showToast(message: String) {
        runOnUiThread {
            Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        private const val CREATE_ACTIVITY = 1
    }
}
