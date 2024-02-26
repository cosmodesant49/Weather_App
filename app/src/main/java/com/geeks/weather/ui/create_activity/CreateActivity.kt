package com.geeks.weather.ui.create_activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.geeks.weather.databinding.ActivityCreateBinding

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
            val intent = Intent().putExtra("city", cityName)
            setResult(Activity.RESULT_OK, intent)
            finish()

        }
    }
}