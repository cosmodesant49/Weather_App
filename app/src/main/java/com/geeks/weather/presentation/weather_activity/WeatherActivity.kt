package com.geeks.weather.presentation.weather_activity

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.room.Room
import com.geeks.weather.data.Resource
import com.geeks.weather.data.db.WeatherDao
import com.geeks.weather.data.db.WeatherDatabase
import com.geeks.weather.data.db.WeatherEntity
import com.geeks.weather.databinding.ActivityMainBinding
import com.geeks.weather.presentation.create_activity.CreateActivity
import com.geeks.weather.presentation.weather_activity.adapter.WeatherAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class WeatherActivity : AppCompatActivity() {

    //private lateinit var retrofitService: RetrofitService
    private lateinit var binding: ActivityMainBinding
    private val viewModel: WeatherViewModel by viewModels()
    private val list = mutableListOf<WeatherEntity>()
    private val adapter = WeatherAdapter() { weatherModel, position ->
        onClickItem(weatherModel, position)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initClickers()

        var cityName = intent.getStringExtra("key") ?: ""
        if (cityName.isNotEmpty()) {
            viewModel.getWeather(cityName).observe(this, Observer { weather ->
                when (weather) {
                    is Resource.Error -> {
                        Log.e("ololo", "Error appeared")
                    }

                    is Resource.Loading -> {
                        Log.e("ololo", "Loading in process: ")
                    }

                    is Resource.Success -> {
                        Log.e(
                            "ololo",
                            "Model:$weather, Data: ${weather.data}, Message: ${weather.message}",
                        )
                        weather?.let {
                            val weatherEntity = WeatherEntity(
                                    cityName = it.data?.name ?: "",
                                    temperature = it.data?.main?.temp ?: 0.0
                                )
                            CoroutineScope(Dispatchers.IO).launch {
                                Log.e("ololo", "Entity: $weatherEntity, Model: $weather")
                                viewModel.insertWeather(weatherEntity)
                            }
                        }
                    }
                }

            })
        }

        binding.rvWeather.adapter = adapter

        CoroutineScope(Dispatchers.Main).launch {
            viewModel.weatherEntities.observe(this@WeatherActivity, Observer { list ->
                adapter.submitList(list)
                adapter.notifyDataSetChanged()
            })
            }

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
                CoroutineScope(Dispatchers.IO).launch {
                    viewModel.deleteWeather(weatherEntity = weatherModel)
                    withContext(Dispatchers.Main){
                        adapter.notifyDataSetChanged()
                    }
                }
            }
            .show()
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
