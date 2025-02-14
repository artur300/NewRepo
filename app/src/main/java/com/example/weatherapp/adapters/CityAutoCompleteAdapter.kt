package com.example.weatherapp.adapters

import android.content.Context
import android.widget.ArrayAdapter
import android.widget.Filter
import com.example.weatherapp.models.CityResponse

class CityAutoCompleteAdapter(context: Context, private var cityList: List<CityResponse>) :
    ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line) {

    private var filteredCities: List<CityResponse> = cityList

    override fun getCount(): Int = filteredCities.size
    override fun getItem(position: Int): String = filteredCities[position].name

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val results = FilterResults()
                if (!constraint.isNullOrEmpty()) {
                    val filtered = cityList.filter { it.name.startsWith(constraint.toString(), ignoreCase = true) }
                    results.values = filtered
                    results.count = filtered.size
                }
                return results
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredCities = results?.values as? List<CityResponse> ?: emptyList()
                notifyDataSetChanged()
            }
        }
    }

    fun updateCities(newCities: List<CityResponse>) {
        cityList = newCities
        filteredCities = newCities
        notifyDataSetChanged()
    }
}


