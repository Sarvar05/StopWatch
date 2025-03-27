package com.example.myapplication.news.retrofit

import com.example.myapplication.news.data.NewsResponse
import retrofit2.http.GET
import retrofit2.http.Query


interface NewsApiService {
    @GET("top-headlines")
    suspend fun getTopHeadlines(
        @Query("country") country: String,
        @Query("apiKey") apiKey: String
    ): NewsResponse
}
