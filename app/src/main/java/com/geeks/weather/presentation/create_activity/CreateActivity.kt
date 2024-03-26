package com.geeks.weather.presentation.create_activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.geeks.weather.data.Resource
import com.geeks.weather.data.db.WeatherEntity
import com.geeks.weather.data.model.WeatherModel
import com.geeks.weather.databinding.ActivityCreateBinding
import com.geeks.weather.presentation.weather_activity.WeatherActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.log

@AndroidEntryPoint
class CreateActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateBinding
    private val viewModel: CreateActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initClickers()
    }


    private fun initClickers() {
        binding.ivSearch.setOnClickListener {
            val cityName = binding.etCity.text.toString().trim()
           /* if (cityName.isNotEmpty()) {
                viewModel.getWeather(cityName).observe(this, Observer { weather ->
                    when (weather) {
                        is Resource.Error -> {
                            Log.e("ololo", "Error appeared")
                        }

                        is Resource.Loading -> {
                            Log.e("ololo", "Loading in process: ", )
                        }
                        is Resource.Success -> {
                            Log.e(
                                "ololo",
                                "Model:$weather, Data: ${weather.data}, Message: ${weather.message}",
                            )
                            weather.data?.let {
                                val weatherEntity = WeatherEntity(
                                    cityName = it.name,
                                    temperature = it.main.temp
                                )
                                CoroutineScope(Dispatchers.IO).launch {
                                    Log.e("ololo", "Entity: $weatherEntity, Model: $weather")
                                    viewModel.insertWeather(weatherEntity)
                                }
                            }
                        }
                    }

                })
            }*/

            val intent = Intent(this, WeatherActivity::class.java)
            intent.putExtra("key", cityName)
            startActivity(intent)
        }
    }
}
