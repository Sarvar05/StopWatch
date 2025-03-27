package com.example.myapplication.news.data


data class NewsResponse(
    val status: String,
    val articles: List<NewsArticle>
)
