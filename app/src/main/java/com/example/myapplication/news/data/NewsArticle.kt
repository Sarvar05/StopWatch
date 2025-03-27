package com.example.myapplication.news.data

data class NewsArticle(
    val title: String,
    val description: String?,
    val urlToImage: String?,
    val publishedAt: String,
    val url: String
)