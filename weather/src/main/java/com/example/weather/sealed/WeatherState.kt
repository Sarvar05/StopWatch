package com.example.weather.sealed

sealed class WeatherState {
    object Loading : WeatherState()
    data class Success(val temperature: Float, val humidity: Int, val windSpeed: Float, val description: String) : WeatherState()
    data class Error(val message: String) : WeatherState()
}
