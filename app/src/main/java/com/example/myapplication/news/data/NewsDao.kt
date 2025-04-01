package com.example.myapplication.news.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface NewsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNews(news: List<NewsArticle>)

    @Query("SELECT * FROM news WHERE isFavorite = 1")
    fun getFavoriteNews(): Flow<List<NewsArticle>>

    @Query("SELECT * FROM news")
    fun getAllNews(): Flow<List<NewsArticle>>

    @Update
    suspend fun updateFavorite(newsArticle: NewsArticle)

}

