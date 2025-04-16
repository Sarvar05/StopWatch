package com.example.weather.presentation

import com.example.weather.domain.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

const val API_KEY_WEATHER = "2446e3823cfb46b7bc8d950370133f9c"

interface WeatherService {

    @GET("weather")
    suspend fun getWeather(
        @Query("q") city: String,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric"
    ): WeatherResponse

}