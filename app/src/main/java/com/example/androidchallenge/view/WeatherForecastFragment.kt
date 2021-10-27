package com.example.androidchallenge.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.androidchallenge.databinding.FragmentWeatherForecastBinding
import com.example.androidchallenge.network.RetrofitClient

class WeatherForecastFragment : Fragment() {

    private lateinit var binding: FragmentWeatherForecastBinding
    private lateinit var weatherViewModel: WeatherViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        weatherViewModel = ViewModelProvider(requireActivity(),
            WeatherViewModelFactory(RetrofitClient.getWeatherApi(), requireActivity().application))
            .get(WeatherViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWeatherForecastBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        weatherViewModel.daily.observe(viewLifecycleOwner, { daily ->
            val adapter = WeatherForecastAdapter()
            binding.rvWeatherForecastWeather.adapter = adapter
            binding.rvWeatherForecastWeather.addItemDecoration(
                DividerItemDecoration(requireActivity(),
                (binding.rvWeatherForecastWeather.layoutManager as LinearLayoutManager).orientation)
            )

            adapter.updateList(daily)
        })
    }
}