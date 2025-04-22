package com.example.weather.data


data class WeatherResponse(
    val main: Main,
    val wind: Wind,
    val weather: List<Weather>
)