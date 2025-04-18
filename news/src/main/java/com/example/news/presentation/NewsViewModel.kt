package com.example.news.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.news.data.NewsArticle
import com.example.news.data.NewsRepository
import com.example.news.data.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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

    private val _toastMessage = MutableLiveData<String?>()

    init {
        loadNews("All")
        getNewsByCategory("All")
        loadFavorites()
    }

    fun loadNews(category: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _isLoading.emit(true)

                val newsResponse = repository.fetchNewsFromApi(category)

                if (newsResponse.status.lowercase() == "ok" && newsResponse.articles.isNotEmpty()) {
                    val updatedArticles = newsResponse.articles.map { it.copy(category = category) }
                    repository.refreshNews(updatedArticles)
                    _newsList.emit(updatedArticles)
                } else {
                    loadCachedNews(category)
                    withContext(Dispatchers.Main) {
                        _toastMessage.value = "The server response does not contain any news"
                    }
                }

            } catch (e: IOException) {
                loadCachedNews(category)
                withContext(Dispatchers.Main) {
                    _toastMessage.value = "Problem connecting to the internet"
                }
            } catch (e: HttpException) {
                loadCachedNews(category)
                val message = when (e.code()) {
                    400 -> "Invalid request (400)"
                    401 -> "Unauthorized. Check API key.(401)"
                    403 -> "Access Denied(403)"
                    404 -> "No news found (404)"
                    500 -> "Server error (500). Try again later"
                    else -> "HTTP error: ${e.code()}"
                }
                withContext(Dispatchers.Main) {
                    _toastMessage.value = message
                }
            } catch (e: Exception) {
                loadCachedNews(category)
                withContext(Dispatchers.Main) {
                    _toastMessage.value = "Unknown error: ${e.localizedMessage}"
                }
            } finally {
                _isLoading.emit(false)
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

                if (response.status.lowercase() == "ok") {
                    val favorites = repository.getFavoriteArticles()
                    val updatedArticles = response.articles.map { article ->
                        article.copy(
                            category = category,
                            isFavorite = favorites.any { it.title == article.title }
                        )
                    }
                    _newsList.value = updatedArticles
                    insertNewsWithErrorHandling(updatedArticles)
                } else {
                    loadCachedNews(category)
                    _toastMessage.postValue("The server response does not contain any news.")
                }
            } catch (e: IOException) {
                loadCachedNews(category)
                _toastMessage.postValue("Problem with internet connection")
            } catch (e: HttpException) {
                loadCachedNews(category)
                val message = when (e.code()) {
                    400 -> "Invalid request(400)"
                    401 -> "Unauthorized. Check API key.(401)"
                    403 -> "Access Denied (403)"
                    404 -> "No news found(404)"
                    500 -> "Server error (500). Try again later."
                    else -> "HTTP error: ${e.code()}"
                }
                _toastMessage.postValue(message)
            } catch (e: Exception) {
                loadCachedNews(category)
                _toastMessage.postValue("An unknown error occurred: ${e.localizedMessage}")
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
            repository.updateArticleFavoriteStatus(
                updatedArticle.url,
                updatedArticle.isFavorite
            )
            _newsList.update { currentList ->
                currentList.map {
                    if (it.url == updatedArticle.url) updatedArticle else it
                }
            }
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
