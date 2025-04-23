package com.example.weather.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather.BuildConfig
import com.example.weather.retrofit.RetrofitInstance
import com.example.weather.sealed.WeatherState
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class WeatherViewModel : ViewModel() {

    private val _weatherState = MutableStateFlow<WeatherState>(WeatherState.Loading)
    val weatherState: StateFlow<WeatherState> = _weatherState

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        _weatherState.value = WeatherState.Error(throwable.localizedMessage ?: "Unknown error")
    }

    fun fetchWeather() {
        viewModelScope.launch(coroutineExceptionHandler) {
            try {
                val response = RetrofitInstance.api.getWeather("Tashkent", BuildConfig.API_KEY)

                _weatherState.value = WeatherState.Success(
                    temperature = response.main.temp,
                    humidity = response.main.humidity,
                    windSpeed = response.wind.speed,
                    description = response.weather.firstOrNull()?.description ?: "Unknown"
                )
            } catch (e: Exception) {
                val errorMessage = when (e) {
                    is IOException -> "No internet connection! "
                    is HttpException -> when (e.code()) {
                        401 -> "Unauthorized (401)"
                        403 -> "Forbidden (403)"
                        404 -> "Not found (404)"
                        500 -> "Server error (500)"
                        else -> "Error try again!"
                    }
                    else -> "Error try again!."
                }
                _weatherState.value = WeatherState.Error(errorMessage)
            }
        }
    }
}

