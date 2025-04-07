package com.example.myapplication.news.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

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

    @Update
    suspend fun updateArticle(article: NewsArticle)

    @Query("SELECT * FROM news WHERE id = :id LIMIT 1")
    suspend fun getArticleById(id: Int): NewsArticle?

    @Query("UPDATE news SET isFavorite = :isFavorite WHERE id = :articleId")
    suspend fun setFavoriteStatus(articleId: Int, isFavorite: Boolean)

}