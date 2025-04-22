package com.example.news.data.repository

import com.example.news.BuildConfig
import com.example.news.data.local.NewsArticle
import com.example.news.data.local.NewsDao
import com.example.news.data.remote.NewsApiService
import com.example.news.domain.NewsResponse
import com.example.news.domain.Resource
import retrofit2.HttpException
import java.io.IOException

class NewsRepository(
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
        return try {
            val response = if (category == "All") {
                apiService.getTopHeadlines("us", BuildConfig.API_KEY)
            } else {
                apiService.getNewsByCategory(category.lowercase(), "us", BuildConfig.API_KEY)
            }

            if (response.status.lowercase() == "ok") {
                Resource.Success(response)
            } else {
                Resource.Error("Invalid response status: ${response.status}")
            }
        } catch (e: IOException) {
            Resource.Error("No internet connection", e)
        } catch (e: HttpException) {
            val message = getHttpErrorMessage(e.code())
            Resource.Error(message, e)
        } catch (e: Exception) {
            Resource.Error("Unexpected error: ${e.localizedMessage}", e)
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
            400 -> "Invalid request (400)"
            401 -> "Unauthorized (401)"
            403 -> "Forbidden (403)"
            404 -> "Not found (404)"
            500 -> "Server error (500)"
            else -> "HTTP error: $code"
        }
    }
}