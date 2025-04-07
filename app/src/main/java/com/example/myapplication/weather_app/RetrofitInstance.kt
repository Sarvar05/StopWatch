package com.example.myapplication.weather_app

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitInstance {

    private const val BASE_URL = "https://api.openweathermap.org/data/2.5/"
    private const val API_KEY_WEATHER = "2446e3823cfb46b7bc8d950370133f9c"

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val apiKeyInterceptor = Interceptor { chain ->
        val original = chain.request()
        val originalUrl = original.url

        val url = originalUrl.newBuilder()
            .addQueryParameter("appid", API_KEY_WEATHER)
            .build()

        val requestBuilder = original.newBuilder().url(url)
        val request = requestBuilder.build()
        chain.proceed(request)
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(apiKeyInterceptor)
        .addInterceptor(loggingInterceptor)
        .build()

    val api: WeatherService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WeatherService::class.java)
    }
}
