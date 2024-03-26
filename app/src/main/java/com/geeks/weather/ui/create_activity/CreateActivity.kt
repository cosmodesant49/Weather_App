package com.geeks.weather.ui.create_activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.geeks.weather.databinding.ActivityCreateBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CreateActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initClickers()
    }

    private fun initClickers() {
        binding.ivSearch.setOnClickListener {
            val cityName = binding.etCity.text.toString()
            CoroutineScope(Dispatchers.Main).launch {
                handleSearch(cityName)
            }
        }
    }

    private suspend fun handleSearch(cityName: String) {
        val intent = Intent().putExtra("city", cityName)
        setResult(Activity.RESULT_OK, intent)
        finish()
        Log.e("ololo", "createActivity: $cityName")
    }
}
