package com.example.news.data

import android.content.Context
import androidx.room.Room


object DatabaseProvider {
    private lateinit var database:AppDatabase
    private lateinit var newsRepository: NewsRepository

    fun init(context: Context) {
        database = Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "news_database"
        )
            .addMigrations(MIGRATION_2_3)
            .build()
        newsRepository = NewsRepository(database.newsDao(), RetrofitInstance.api)
    }

    fun getRepository(): NewsRepository {
        return newsRepository
    }
}