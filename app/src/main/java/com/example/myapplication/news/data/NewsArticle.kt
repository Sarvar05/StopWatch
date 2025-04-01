package com.example.myapplication.news.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "news")
data class NewsArticle(
    @PrimaryKey
    val title: String,
    val description: String?,
    val urlToImage: String?,
    val publishedAt: String,
    val url: String,
    val content: String,
    val category: String,
    var isFavorite: Boolean = false
)