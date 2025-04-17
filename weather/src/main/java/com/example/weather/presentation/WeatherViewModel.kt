package com.example.weather.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather.domain.RetrofitInstance
import kotlinx.coroutines.launch

class WeatherViewModel : ViewModel() {
    var temperature by mutableFloatStateOf(0f)
        private set
    var humidity by mutableIntStateOf(0)
        private set
    var windSpeed by mutableFloatStateOf(0f)
        private set
    var description by mutableStateOf("Loading...")
        private set

    fun fetchWeather(city: String, apiKey: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.getWeather(city, apiKey)
                temperature = response.main.temp
                humidity = response.main.humidity
                windSpeed = response.wind.speed
                description = response.weather.firstOrNull()?.description ?: "Unknown"
            } catch (e: Exception) {
                description = "Error: ${e.message}"
            }
        }
    }
}
