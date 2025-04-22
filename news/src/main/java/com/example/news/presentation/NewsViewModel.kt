package com.example.news.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.news.data.exception.CategoryException
import com.example.news.data.local.NewsArticle
import com.example.news.data.repository.NewsRepository
import com.example.news.domain.Resource
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

    private val _favoriteNewsList = MutableStateFlow<List<NewsArticle>>(emptyList())
    val favoriteNewsList: StateFlow<List<NewsArticle>> = _favoriteNewsList

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _toastMessage = MutableLiveData<String?>()


    init {
        loadNews("All")
        loadFavorites()
    }

    fun loadNews(category: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.emit(true)

            when (val result = repository.fetchNewsFromApi(category)) {
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
                    val error = result.throwable
                    _toastMessage.postValue(
                        when (error) {
                            is CategoryException -> "Category error'${error.category}': ${result.message}"
                            else -> "Error: ${result.message}"
                        }
                    )
                    handleError(error ?: Exception(result.message), category)
                }
            }

            _isLoading.emit(false)
        }
    }


    private suspend fun handleError(e: Throwable, category: String) {
        val message = when (e) {
            is IOException -> "No internet connection"
            is HttpException -> repository.getHttpErrorMessage(e.code())
            else -> "Unexpected error: ${e.localizedMessage}"
        }

        val cachedNews = repository.getNewsByCategory(category)
        _newsList.emit(cachedNews)

        withContext(Dispatchers.Main) {
            _toastMessage.value = message
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
