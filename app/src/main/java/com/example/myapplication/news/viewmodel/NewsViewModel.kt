package com.example.myapplication.news.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.launch
import com.example.myapplication.news.data.NewsArticle
import androidx.lifecycle.viewModelScope
import com.example.myapplication.news.data.NewsRepository
import com.example.myapplication.news.service.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import retrofit2.HttpException
import java.io.IOException

class NewsViewModel(
    private val repository: NewsRepository
) : ViewModel() {

    private val _newsList = MutableStateFlow<List<NewsArticle>>(emptyList())
    val newsList: StateFlow<List<NewsArticle>> = _newsList

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _favoriteNewsList = MutableStateFlow<List<NewsArticle>>(emptyList())
    val favoriteNewsList: StateFlow<List<NewsArticle>> = _favoriteNewsList

    init {
        loadNews("All")
        getNewsByCategory("All")
        loadFavorites()
    }

    fun loadNews(category: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true

            try {
                Log.d("NewsViewModel", "Fetching news for category: $category")
                val newsResponse = repository.fetchNewsFromApi(category)

                if (newsResponse.status == "ok") {
                    val newsWithCategory = newsResponse.articles.map {
                        it.copy(category = category)
                    }

                    _newsList.value = newsWithCategory
                    repository.insertNews(newsWithCategory)
                } else {
                    Log.e("NewsViewModel", "Error fetching news: ${newsResponse.status}")
                    loadCachedNews(category)
                }
            } catch (e: Exception) {
                Log.e("NewsViewModel", "Error fetching news: ${e.message}")
                loadCachedNews(category)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun getNewsByCategory(category: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true
            try {
                val response = if (category == "All") {
                    RetrofitInstance.api.getTopHeadlines(
                        country = "us",
                        apiKey = "922359aac397424a9c3e73a984948bce"
                    )
                } else {
                    RetrofitInstance.api.getNewsByCategory(
                        category = category.lowercase(),
                        country = "us",
                        apiKey = "922359aac397424a9c3e73a984948bce"
                    )
                }

                if (response.status == "ok") {
                    _newsList.value = response.articles
                    insertNewsWithErrorHandling(response.articles.map { it.copy(category = category) })
                } else {
                    Log.e("NewsViewModel", "Error fetching news: ${response.status}")
                    loadCachedNews(category)
                }
            } catch (e: HttpException) {
                Log.e("NewsViewModel", "HttpException: ${e.message}")
                loadCachedNews(category)
            } catch (e: IOException) {
                Log.e("NewsViewModel", "IOException: ${e.message}")
                loadCachedNews(category)
            } finally {
                _isLoading.value = false
            }
        }
    }


    private fun loadCachedNews(category: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val cachedNews = repository.getNewsByCategory(category)
            _newsList.value = cachedNews
            if (cachedNews.isEmpty()) {
                Log.d("NewsViewModel", "No cached news available for category: $category")
            } else {
                Log.d("NewsViewModel", "Loaded cached news for category: $category")
            }
        }
    }


    private fun loadFavorites() {
        viewModelScope.launch(Dispatchers.IO) {
            val favoriteArticles = repository.getFavoriteArticles()
            _favoriteNewsList.value = favoriteArticles
            Log.d("Favorites", "Loaded ${favoriteArticles.size} favorites from DB")
        }
    }


    fun toggleFavorite(article: NewsArticle) {
        viewModelScope.launch(Dispatchers.IO) {
            val updatedArticle = article.copy(isFavorite = !article.isFavorite)

            repository.updateArticleFavoriteStatus(updatedArticle.id, updatedArticle.isFavorite)

            loadFavorites()
        }
    }



    suspend fun insertNewsWithErrorHandling(newsList: List<NewsArticle>) {
        try {
            if (newsList.isNotEmpty()) {
                Log.d("Database", "Let's start inserting ${newsList.size} articles")
                newsList.forEach { article ->
                    Log.d(
                        "Database",
                        "The article is inserted: ${article.title}, category: ${article.category}"
                    )
                }

                repository.insertNews(newsList)

                Log.d("Database", "All articles have been successfully inserted")
            } else {
                Log.e("Database", "The news list is empty - nothing to insert")
            }
        } catch (e: Exception) {
            Log.e("Database", "Error inserting news: ${e.message}")
            e.printStackTrace()
        }
    }
}




