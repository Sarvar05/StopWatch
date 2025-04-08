package com.example.myapplication.news.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface NewsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNews(news: List<NewsArticle>)

    @Query("DELETE FROM news")
    suspend fun deleteAllNews()

    @Query("SELECT * FROM news WHERE category = :category")
    suspend fun getNewsByCategory(category: String): List<NewsArticle>

    @Query("SELECT * FROM news WHERE isFavorite = 1")
    suspend fun getFavoriteNews(): List<NewsArticle>

    @Query("DELETE FROM news WHERE category = :category")
    suspend fun deleteNewsByCategory(category: String)

    @Query("SELECT * FROM news WHERE url = :url LIMIT 1")
    suspend fun getArticleByUrl(url: String): NewsArticle?

    @Query("UPDATE news SET isFavorite = :isFavorite WHERE url = :url")
    suspend fun setFavoriteStatus(url: String, isFavorite: Boolean)

    @Query("DELETE FROM news WHERE isFavorite = 0")
    suspend fun clearNonFavoriteArticles()
}
