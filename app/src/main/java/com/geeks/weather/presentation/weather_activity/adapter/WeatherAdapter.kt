package com.geeks.weather.presentation.weather_activity.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.geeks.weather.databinding.ItemCityBinding
import com.geeks.weather.data.db.WeatherEntity

class WeatherAdapter(
    private val onLongClickItem: (weather: WeatherEntity, position: Int) -> Unit,
) : RecyclerView.Adapter<WeatherAdapter.ViewHolder>() {

    private var list: ArrayList<WeatherEntity> = arrayListOf()

    fun submitList(list:List<WeatherEntity>){
        this.list.clear()
        this.list.addAll(list)
      //  notifyDataSetChanged()
    }
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