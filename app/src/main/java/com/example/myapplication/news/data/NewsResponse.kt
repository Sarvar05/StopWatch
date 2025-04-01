package com.example.myapplication.news.data


data class NewsResponse(
    val status: String,
    val totalResults: Int,
    val articles: List<NewsArticle>
)
