package com.example.news.domain

import com.example.news.data.NewsArticle

data class NewsResponse(
    val status: String,
    val totalResults: Int,
    val articles: List<NewsArticle>
)