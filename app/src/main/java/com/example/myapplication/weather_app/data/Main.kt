package com.example.myapplication.weather_app.data

import com.google.gson.annotations.SerializedName

data class Main(
    val temp: Float,
    //TODO type naming rule
    //TODO what is lint rules?
//    @SerializedName
    val temp_min: Float,
    val temp_max: Float,
    val humidity: Int
)