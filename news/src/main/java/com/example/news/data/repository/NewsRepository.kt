package com.example.news.data.repository

import com.example.news.BuildConfig
import com.example.news.data.exception.ApiException
import com.example.news.data.local.NewsArticle
import com.example.news.data.local.NewsDao
import com.example.news.data.service.NewsApiService
import com.example.news.domain.NewsResponse
import com.example.news.utils.Resource
import com.example.news.utils.toCategoryError
import com.example.news.utils.toErrorMessage

class NewsRepository (
    private val newsDao: NewsDao,
    private val apiService: NewsApiService
) {

    suspend fun getNewsByCategory(category: String): List<NewsArticle> {
        return newsDao.getNewsByCategory(category)
    }

    suspend fun getFavoriteArticles(): List<NewsArticle> {
        return newsDao.getFavoriteNews()
    }

    suspend fun fetchNewsFromApi(category: String): Resource<NewsResponse> {
        return runCatching {
            val response = if (category == "All") {
                apiService.getTopHeadlines("us", BuildConfig.API_KEY)
            } else {
                apiService.getNewsByCategory(category.lowercase(), "us", BuildConfig.API_KEY)
            }

            if (response.status.lowercase() != "ok") {
                throw ApiException(500, "Invalid response status: ${response.status}")
            }

            Resource.Success(response)
        }.getOrElse { throwable ->
            Resource.Error(
                message = throwable.toErrorMessage(this),
                throwable = throwable.toCategoryError(category)
            )
        }
    }

    suspend fun updateArticleFavoriteStatus(url: String, isFavorite: Boolean) {
        newsDao.setFavoriteStatus(url, isFavorite)
    }

    suspend fun refreshNews(articles: List<NewsArticle>) {
        if (articles.isNotEmpty()) {
            val category = articles.first().category ?: "All"
            newsDao.deleteNewsByCategory(category)
            newsDao.insertNews(articles)
        }
    }

    fun getHttpErrorMessage(code: Int): String {
        return when (code) {
            401 -> "Unauthorized (401)"
            403 -> "Forbidden (403)"
            404 -> "Not found (404)"
            500 -> "Server error (500)"
            else -> "HTTP error: $code"
        }
    }
}