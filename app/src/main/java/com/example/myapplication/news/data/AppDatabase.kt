package com.example.myapplication.news.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [NewsArticle::class], version = 2)

abstract class AppDatabase : RoomDatabase() {
    abstract fun newsDao(): NewsDao
}
