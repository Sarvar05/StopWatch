package com.example.myapplication.news.data
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
        return newsDao.getFavoriteNews()
    }

    suspend fun insertNews(newsList: List<NewsArticle>) {
        val uniqueArticles = newsList.distinctBy { it.url }
        uniqueArticles.forEach { article ->
            val existingArticle = newsDao.getArticleByUrl(article.url.toString())
            if (existingArticle == null) {
                newsDao.insertNews(listOf(article))
            }
        }
    }

    suspend fun fetchNewsFromApi(category: String): NewsResponse {
        return if (category == "All") {
            apiService.getTopHeadlines("us", API_KEY)
        } else {
            apiService.getNewsByCategory(category.lowercase(), "us", API_KEY)
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
}
