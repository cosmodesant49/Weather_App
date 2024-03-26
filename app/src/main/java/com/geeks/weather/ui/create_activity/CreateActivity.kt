package com.geeks.weather.ui.create_activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.geeks.weather.databinding.ActivityCreateBinding

class CreateActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateBinding
    private val viewModel: CreateActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initObservers()
        initClickers()
    }

    private fun initObservers() {
        viewModel.searchResult.observe(this, Observer { result ->
            result.fold(
                onSuccess = { cityName ->
                    val intent = Intent().putExtra("city", cityName)
                    setResult(Activity.RESULT_OK, intent)
                    finish()
                    Log.e("CreateActivity", "City selected: $cityName")
                },
                onFailure = { error ->
                    // Handle the error case, e.g., show an error message
                    binding.etCity.error = error.message
                    Log.e("CreateActivity", "Error searching city: ${error.message}")
                }
            )
        })
    }

    private fun initClickers() {
        binding.ivSearch.setOnClickListener {
            val cityName = binding.etCity.text.toString().trim()
            viewModel.searchCity(cityName)
        }
    }
}
