package com.example.weatherapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.R
import com.example.weatherapp.models.WeatherResponse

class WeatherAdapter : RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder>(){

    inner class WeatherViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private val differCallback = object : DiffUtil.ItemCallback<WeatherResponse>() {
        override fun areItemsTheSame(oldItem: WeatherResponse, newItem: WeatherResponse): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: WeatherResponse,
            newItem: WeatherResponse
        ): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        return WeatherViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.fragment_weather_local,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        val weather = differ.currentList[position]
        holder.itemView.apply {
            //TODO
        }
    }

    override fun getItemCount() = differ.currentList.size
}