package com.geeks.weather.ui.weather_activity.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.geeks.weather.databinding.ItemCityBinding
import com.geeks.weather.data.db.WeatherEntity
import com.geeks.weather.data.model.WeatherModel

class WeatherAdapter(
    private val list: MutableList<WeatherEntity>,
    private val onLongClickItem: (weather: WeatherEntity, position: Int) -> Unit
) : RecyclerView.Adapter<WeatherAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCityBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val city = list[position]
        holder.bind(city)
    }

    inner class ViewHolder(private val binding: ItemCityBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(weatherModel: WeatherEntity) {
            with(binding) {
                tvCity.text = weatherModel.cityName
                tvTemp.text = "${weatherModel.temperature}°"
                itemView.setOnClickListener {
                    if (position in 0 until list.size) {
                        onLongClickItem(list[adapterPosition], adapterPosition)
                    }
                }
            }
        }
    }
}