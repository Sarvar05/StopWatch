package com.example.weather.data

import com.example.weather.data.Wind

data class WeatherResponse(
    val main: Main,
    val wind: Wind,
    val weather: List<Weather>
)