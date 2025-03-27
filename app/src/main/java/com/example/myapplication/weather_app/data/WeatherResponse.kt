package com.example.myapplication.weather_app.data

data class WeatherResponse(
    val main: Main,
    val wind: Wind,
    val weather: List<Weather>
)