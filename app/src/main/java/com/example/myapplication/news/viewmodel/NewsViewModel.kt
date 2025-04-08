package com.example.myapplication.news.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.launch
import com.example.myapplication.news.data.NewsArticle
import androidx.lifecycle.viewModelScope
import com.example.myapplication.news.data.API_KEY
import com.example.myapplication.news.data.NewsRepository
import com.example.myapplication.news.service.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

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
                val newsResponse = repository.fetchNewsFromApi(category)
                if (newsResponse.status == "ok") {
                    repository.refreshNews(newsResponse.articles.map { it.copy(category = category) })
                    _newsList.value = newsResponse.articles
                } else {
                    loadCachedNews(category)
                }
            } catch (e: Exception) {
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
                    RetrofitInstance.api.getTopHeadlines("us", API_KEY)
                } else {
                    RetrofitInstance.api.getNewsByCategory(category.lowercase(), "us", API_KEY)
                }
                if (response.status == "ok") {
                    _newsList.value = response.articles
                    insertNewsWithErrorHandling(response.articles.map { it.copy(category = category) })
                } else {
                    loadCachedNews(category)
                }
            } catch (e: Exception) {
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
        }
    }

    private fun loadFavorites() {
        viewModelScope.launch(Dispatchers.IO) {
            val favoriteArticles = repository.getFavoriteArticles()
            _favoriteNewsList.value = favoriteArticles
        }
    }

    fun toggleFavorite(article: NewsArticle) {
        viewModelScope.launch(Dispatchers.IO) {
            val updatedArticle = article.copy(isFavorite = !article.isFavorite)
            repository.updateArticleFavoriteStatus(updatedArticle.url.toString(), updatedArticle.isFavorite)
            loadFavorites()
        }
    }

    suspend fun insertNewsWithErrorHandling(newsList: List<NewsArticle>) {
        try {
            if (newsList.isNotEmpty()) {
                repository.insertNews(newsList)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
