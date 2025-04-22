package com.example.news.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.news.data.local.NewsDao

@Database(entities = [NewsArticle::class], version = 3)

abstract class AppDatabase : RoomDatabase() {
    abstract fun newsDao(): NewsDao
}