package com.geeks.weather.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.geeks.weather.databinding.ItemCityBinding
import com.geeks.weather.model.WeatherModel

class WeatherAdapter(
    private val list: MutableList<WeatherModel>,
    private val onLongClickItem: (weather: WeatherModel) -> Unit
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

    inner class ViewHolder(private val binding: ItemCityBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(weatherModel: WeatherModel) {
            with(binding) {
                tvCity.text = weatherModel.name
                tvTemp.text = "${weatherModel.main.temp.toInt()}°"
                itemView.setOnClickListener {
                    onLongClickItem(list[adapterPosition])
                }
            }
        }
    }
}
