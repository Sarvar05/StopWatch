package com.example.myapplication.weather_app.data

import com.google.gson.annotations.SerializedName

data class Main(
    val temp: Float,
    @SerializedName("temp_min") val tempMin: Float,
    @SerializedName("temp_max") val tempMax: Float,
    val humidity: Int
)