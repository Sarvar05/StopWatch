package com.example.news.data

import com.example.news.domain.NewsArticleDto

fun NewsArticleDto.toNewsArticle(category: String):NewsArticle {
    return NewsArticle(
        title = this.title ?: "",
        description = this.description ?: "",
        url = this.url ?: "",
        urlToImage = this.urlToImage ?: "",
        publishedAt = this.publishedAt ?: "",
        category = category,
        isFavorite = false
    )
}

