package com.example.news.data

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object
RetrofitInstance {
    private const val BASE_URL = "https://newsapi.org/v2/"
    private const val TIMEOUT_MINUTES=60L

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(TIMEOUT_MINUTES, TimeUnit.SECONDS)
        .readTimeout(TIMEOUT_MINUTES, TimeUnit.SECONDS)
        .build()

    val api: NewsApiService = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(NewsApiService::class.java)
}
