package com.example.weatherapp.ui.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.weatherapp.R
import com.example.weatherapp.adapters.CityAutoCompleteAdapter
import com.example.weatherapp.databinding.FragmentCitySearchBinding
import com.example.weatherapp.ui.CitySearchViewModel
import com.example.weatherapp.util.Resource
import dagger.hilt.android.AndroidEntryPoint
import il.co.syntax.fullarchitectureretrofithiltkotlin.utils.autoCleared
import com.example.weatherapp.util.convertUnixToTime
import java.util.Locale

@AndroidEntryPoint
class CitySearchFragment : Fragment() {

    private var binding: FragmentCitySearchBinding by autoCleared()
    private val viewModel: CitySearchViewModel by viewModels()
    private lateinit var cityAdapter: CityAutoCompleteAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCitySearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
        observeWeatherData()
        observeCityList()


        binding.ivHeartIcon.setOnClickListener {
            val weather = viewModel.weatherData.value?.data
            if (weather != null) {
                viewModel.saveWeatherToFavorites(weather)
            }
        }


        binding.btnGoToFavorites.setOnClickListener {
            findNavController().navigate(R.id.action_citySearchFragment_to_favoritesFragment)
        }

        // עדכון רשימת הערים בזמן אמת תוך כדי הקלדה
        binding.etCityName.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!s.isNullOrEmpty()) {
                    viewModel.searchCities(s.toString()) // חיפוש בזמן אמת
                }
            }
        })

        // הסרת הקריאה ל-getWeatherByCity בבחירה מהרשימה כדי למנוע חיפוש מיותר
        binding.etCityName.setOnItemClickListener { _, _, position, _ ->
            val selectedCity = cityAdapter.getItem(position)
            binding.etCityName.setText(selectedCity) // רק מכניס את הערך לשדה, לא מעדכן את הכרטיסייה
        }



        // חיפוש יתבצע רק בעת לחיצה על כפתור "חפש"
        binding.btnSearch.setOnClickListener {
            val enteredCity = binding.etCityName.text.toString()
            if (enteredCity.isNotEmpty()) {
                viewModel.getWeatherByCity(enteredCity) // קריאה לפי עיר בלבד
            } else {
                Toast.makeText(requireContext(), getString(R.string.enter_city_name), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun initAdapter() {
        cityAdapter = CityAutoCompleteAdapter(requireContext(), emptyList())
        binding.etCityName.setAdapter(cityAdapter)
    }

    private fun observeCityList() {
        viewModel.cityList.observe(viewLifecycleOwner) { cities ->
            cityAdapter.updateCities(cities)
        }
    }

    private fun observeWeatherData() {
        viewModel.weatherData.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Success -> {
                    val weather = resource.data
                    hideLoading()
                    with(binding) {
                        tvCity.text = getString(R.string.label_city, weather?.name ?: getString(R.string.city_n_a))
                        tvCountry.text = getString(R.string.label_country, Locale("", weather?.sys?.country ?: "").displayCountry)
                        tvTemperature.text = getString(R.string.label_temp, weather?.main?.temp ?: "--")
                        tvFeelsLike.text = getString(R.string.label_feels_like, weather?.main?.feels_like ?: "--")
                        tvHumidity.text = getString(R.string.label_humidity, weather?.main?.humidity ?: "--")
                        tvWindSpeed.text = getString(R.string.label_wind, weather?.wind?.speed ?: "--")
                        tvSunrise.text = getString(R.string.label_sunrise, convertUnixToTime(weather?.sys?.sunrise, weather?.timezone))
                        tvSunset.text = getString(R.string.label_sunset, convertUnixToTime(weather?.sys?.sunset, weather?.timezone))
                        tvWeatherDescription.text = getString(R.string.label_weather_description, weather?.weather?.firstOrNull()?.description ?: "--")

                        val iconCode = weather?.weather?.firstOrNull()?.icon
                        ivWeatherIcon.setImageResource(viewModel.getWeatherIcon(iconCode))
                    }
                }
                is Resource.Error -> {
                    hideLoading()
                    Toast.makeText(requireContext(), resource.message ?: getString(R.string.error_fetch_weather), Toast.LENGTH_SHORT).show()
                }
                is Resource.Loading -> {
                    showLoading()
                }
            }
        }
    }

    private fun showLoading() {
        binding.tvCity.text = getString(R.string.loading)
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        binding.progressBar.visibility = View.GONE
    }


}

