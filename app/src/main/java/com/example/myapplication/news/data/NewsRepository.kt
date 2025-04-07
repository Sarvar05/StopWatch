package com.example.myapplication.news.data

import android.util.Log
import com.example.myapplication.news.service.NewsApiService


const val API_KEY = "922359aac397424a9c3e73a984948bce"

class NewsRepository(
    private val newsDao: NewsDao,
    private val apiService: NewsApiService
) {

    suspend fun getNewsByCategory(category: String): List<NewsArticle> {
        return newsDao.getNewsByCategory(category)
    }

    suspend fun getFavoriteArticles(): List<NewsArticle> {
        val favorites = newsDao.getFavoriteNews()
        Log.d("Favorite", "Loaded ${favorites.size} favorites from DB")
        return favorites
    }


    suspend fun insertNews(newsList: List<NewsArticle>) {
        newsDao.insertNews(newsList)
        Log.d("NewsRepository", "Inserted ${newsList.size} articles into the database")
    }


//    suspend fun updateArticle(article: NewsArticle) {
//        newsDao.updateArticle(article)
//    }

    suspend fun fetchNewsFromApi(category: String): NewsResponse {
        return if (category == "All") {
            apiService.getTopHeadlines("us", API_KEY)
        } else {
            apiService.getNewsByCategory(category.lowercase(), "us", API_KEY)
        }
    }

    suspend fun updateArticleFavoriteStatus(id: Int, isFavorite: Boolean) {
        Log.d("Favorite", "Updating article $id favorite to $isFavorite")
        newsDao.setFavoriteStatus(id, isFavorite)
    }


}
