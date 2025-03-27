package com.example.myapplication.news.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.launch
import com.example.myapplication.news.data.NewsArticle
import androidx.lifecycle.viewModelScope
import com.example.myapplication.news.retrofit.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import retrofit2.HttpException
import java.io.IOException

class NewsViewModel : ViewModel() {

    private val _newsList = MutableStateFlow<List<NewsArticle>>(emptyList())
    val newsList: StateFlow<List<NewsArticle>> = _newsList

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        loadNews()
    }
    private fun loadNews() {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true
            try {
                val response = RetrofitInstance.api.getTopHeadlines(
                    country = "us",
                    apiKey = "536080f8cd6741a491a00022b0613784"
                )

                if (response.status == "ok") {
                    _newsList.value = response.articles
                } else {
                    Log.e("NewsViewModel", "Error fetching news: ${response.status}")
                }
            } catch (e: HttpException) {
                Log.e("NewsViewModel", "HttpException: ${e.message}")
            } catch (e: IOException) {
                Log.e("NewsViewModel", "IOException: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

}

