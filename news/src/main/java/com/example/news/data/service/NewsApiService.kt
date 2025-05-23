package com.example.news.data.service

import com.example.news.domain.NewsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApiService {
    @GET("top-headlines")
    suspend fun getTopHeadlines(
        @Query("country") country: String,
        @Query("apiKey") apiKey: String
    ): NewsResponse

    @GET("top-headlines")
    suspend fun getNewsByCategory(
        @Query("category") category: String,
        @Query("country") country: String,
        @Query("apiKey") apiKey: String
    ): NewsResponse
}