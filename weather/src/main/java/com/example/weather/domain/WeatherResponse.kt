package com.example.weather.domain

data class WeatherResponse(
    val main:  Main,
    val wind:  Wind,
    val weather: List< Weather>
)