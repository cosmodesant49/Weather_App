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
import com.geeks.weather.ui.create_activity.CreateActivity
import com.geeks.weather.retrofit.RetrofitService
import com.geeks.weather.ui.weather_activity.adapter.WeatherAdapter
import com.geeks.weather.databinding.ActivityMainBinding
import com.geeks.weather.db.App
import com.geeks.weather.db.WeatherDao
import com.geeks.weather.db.WeatherDatabase
import com.geeks.weather.db.WeatherEntity
import com.geeks.weather.model.WeatherModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WeatherActivity : AppCompatActivity() {

    lateinit var retrofitService: RetrofitService
    private lateinit var binding: ActivityMainBinding
    val list = mutableListOf<WeatherEntity>()
    private val adapter = WeatherAdapter(list) { weatherModel, position ->
        onLongClickItem(weatherModel, position)
    }

    lateinit var weatherDao: WeatherDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val db = Room.databaseBuilder(
            applicationContext,
            WeatherDatabase::class.java,
            "weather-database"
        ).build()

        binding.rvWeather.adapter = adapter

        initClickers()
        CoroutineScope(Dispatchers.IO).launch {
            val savedCities = App.db.weatherDao().getAllWeather()
            list.clear()
            list.addAll(savedCities)
            Log.e("ololo", "createActivity: $savedCities")
            runOnUiThread {
                adapter.notifyDataSetChanged()
            }
        }

    }


    private fun initClickers() {
        binding.ivAddCity.setOnClickListener {
            Log.e("ololo", "initClickers in main: ", )
            val intent = Intent(this, CreateActivity::class.java)
            startActivityForResult(intent, CREATE_ACTIVITY)
        }
    }

    private fun onLongClickItem(weatherModel: WeatherEntity, position: Int) {
        showAlertDialog(weatherModel, position)
    }


    private fun showAlertDialog(weatherModel: WeatherEntity, position: Int) {
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle(weatherModel.cityName)
            .setMessage("Are you sure you want to delete this city?")
            .setCancelable(true)
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton("Delete") { _, _ ->
                CoroutineScope(Dispatchers.IO).launch{
                    Log.e("ololo", "showAlertDialog: $weatherModel", )
                    App.db.weatherDao().deleteWeather(weatherModel)
                    list.clear()
                    list.addAll(App.db.weatherDao().getAllWeather())
                }
                adapter.notifyDataSetChanged()
            }
            .show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CREATE_ACTIVITY && resultCode == Activity.RESULT_OK) {
            data?.getStringExtra("city")?.let { city ->
                Log.e("ololo", "createActivity: $city")
                loadData(city)
            }
        }
    }

    private fun saveCity(cityName: String) {
        val cityEntity = WeatherEntity(cityName = cityName, temperature = 0.0)
        CoroutineScope(Dispatchers.IO).launch {
            weatherDao.insertWeather(cityEntity)
        }
    }


    fun loadData(city: String) {
        RetrofitService().api.getWeather(city).enqueue(object : Callback<WeatherModel> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(call: Call<WeatherModel>, response: Response<WeatherModel>) {
                when {
                    response.isSuccessful -> {
                        response.body()?.let {
                            val weatherEntity =
                                WeatherEntity(cityName = it.name, temperature = it.main.temp)
                            CoroutineScope(Dispatchers.IO).launch {
                            App.db.weatherDao().insertWeather(weatherEntity)
                            }
                            Log.e("ololo", "weather entity: $weatherEntity")
                            CoroutineScope(Dispatchers.IO).launch {
                                val savedCities = App.db.weatherDao().getAllWeather()
                                list.clear()
                                list.addAll(savedCities)
                                Log.e("ololo", "list: $savedCities")
                                runOnUiThread { adapter.notifyDataSetChanged() }
                            }
                        }
                    }

                    else -> {
                        showToast("Error: ${response.code()} - ${response.message()}")
                    }
                }
            }

            override fun onFailure(call: Call<WeatherModel>, t: Throwable) {
                showToast("Error: ${t.localizedMessage}")
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
