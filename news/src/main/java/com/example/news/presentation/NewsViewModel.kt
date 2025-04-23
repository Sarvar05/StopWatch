package com.example.news.presentation

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.news.data.local.NewsArticle
import com.example.news.data.repository.NewsRepository
import com.example.news.utils.Resource
import com.example.news.utils.toErrorMessage
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class NewsViewModel(
    private val repository: NewsRepository
) : ViewModel() {

    private val _newsList = MutableStateFlow<List<NewsArticle>>(emptyList())
    val newsList: StateFlow<List<NewsArticle>> = _newsList

    private val _favoriteNewsList = MutableStateFlow<List<NewsArticle>>(emptyList())
    val favoriteNewsList: StateFlow<List<NewsArticle>> = _favoriteNewsList

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _toastMessage = MutableLiveData<String?>()

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Log.e("NewsViewModel", "Error: ${throwable.localizedMessage}", throwable)
        _toastMessage.postValue("Error: ${throwable.localizedMessage}")
        _isLoading.value = false
    }

    init {
        loadNews("All")
        loadFavorites()
    }

    fun loadNews(category: String) {
        viewModelScope.launch(Dispatchers.Main + coroutineExceptionHandler) {
            _isLoading.emit(true)

            val result = withContext(Dispatchers.IO) {
                repository.fetchNewsFromApi(category)
            }

            when (result) {
                is Resource.Success -> {
                    val favorites = repository.getFavoriteArticles()
                    val updatedArticles = result.data.articles.map { article ->
                        article.copy(
                            category = category,
                            isFavorite = favorites.any { it.url == article.url }
                        )
                    }
                    repository.refreshNews(updatedArticles)
                    _newsList.emit(updatedArticles)
                }

                is Resource.Error -> {
                    val error = result.throwable ?: Exception(result.message)
                    val cachedNews = repository.getNewsByCategory(category)
                    _newsList.emit(cachedNews)
                    _toastMessage.postValue(error.toErrorMessage(repository))
                }
            }

            _isLoading.emit(false)
        }
    }


    fun toggleFavorite(article: NewsArticle) {
        viewModelScope.launch(Dispatchers.IO) {
            val updated = article.copy(isFavorite = !article.isFavorite)
            repository.updateArticleFavoriteStatus(updated.url, updated.isFavorite)

            _newsList.update { list ->
                list.map { if (it.url == updated.url) updated else it }
            }

            loadFavorites()
        }
    }

    private fun loadFavorites() {
        viewModelScope.launch(Dispatchers.IO) {
            val favorites = repository.getFavoriteArticles()
            _favoriteNewsList.emit(favorites)
        }
    }
}
