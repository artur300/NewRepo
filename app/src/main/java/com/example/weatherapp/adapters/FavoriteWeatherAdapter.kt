package com.example.weatherapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.databinding.ItemFavoriteWeatherBinding
import com.example.weatherapp.models.FavoriteWeather
import com.example.weatherapp.ui.CitySearchViewModel
import java.util.Locale

class FavoriteWeatherAdapter(
    private val viewModel: CitySearchViewModel,
    private val onDeleteClick: (FavoriteWeather) -> Unit
) : RecyclerView.Adapter<FavoriteWeatherAdapter.FavoriteViewHolder>() {

    private var favorites: List<FavoriteWeather> = emptyList()

    inner class FavoriteViewHolder(private val binding: ItemFavoriteWeatherBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(favorite: FavoriteWeather) {
            val countryName = Locale("", favorite.country).displayCountry
            binding.tvCityAndCountry.text = "${favorite.cityName}, $countryName" // עכשיו מציג גם מדינה
            binding.tvTemperature.text = "${favorite.temperature}°C"
            binding.tvDescription.text = favorite.description

            // שינוי האייקון
            val iconRes = viewModel.getWeatherIcon(favorite.iconCode)
            binding.ivWeatherIcon.setImageResource(iconRes)

            // מחיקת פריט בלחיצה
            binding.ivDeleteFavorite.setOnClickListener {
                onDeleteClick(favorite)
            }
        }
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val binding = ItemFavoriteWeatherBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoriteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bind(favorites[position])
    }

    override fun getItemCount() = favorites.size

    fun submitList(newList: List<FavoriteWeather>) {
        favorites = newList
        notifyDataSetChanged()
    }
}


