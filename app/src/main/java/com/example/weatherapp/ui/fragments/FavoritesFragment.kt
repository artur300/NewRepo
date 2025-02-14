package com.example.weatherapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherapp.R
import com.example.weatherapp.adapters.FavoriteWeatherAdapter
import com.example.weatherapp.databinding.FragmentFavoritesBinding
import com.example.weatherapp.ui.CitySearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import il.co.syntax.fullarchitectureretrofithiltkotlin.utils.autoCleared

@AndroidEntryPoint
class FavoritesFragment : Fragment() {

    private var binding: FragmentFavoritesBinding by autoCleared()
    private val viewModel: CitySearchViewModel by viewModels()
    private lateinit var favoriteAdapter: FavoriteWeatherAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeFavorites()

        binding.btnGoToSearch.setOnClickListener {
            findNavController().navigate(R.id.action_favoritesFragment_to_citySearchFragment)
        }
    }

    private fun setupRecyclerView() {
        favoriteAdapter = FavoriteWeatherAdapter { favorite ->
            viewModel.removeWeatherFromFavorites(favorite) // קריאה למחיקה
        }
        binding.rvFavorites.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = favoriteAdapter
        }
    }

    private fun observeFavorites() {
        viewModel.favoriteWeatherList.observe(viewLifecycleOwner) { favorites ->
            favoriteAdapter.submitList(favorites)
        }
    }
}
