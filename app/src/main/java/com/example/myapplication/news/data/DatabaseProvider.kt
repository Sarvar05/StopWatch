package com.example.myapplication.news.data

import android.content.Context
import androidx.room.Room
import com.example.myapplication.news.service.RetrofitInstance

object DatabaseProvider {
    private lateinit var database: AppDatabase
    private lateinit var newsRepository: NewsRepository

    fun init(context: Context) {
        database = Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "news_database"
        )
            .addMigrations(MIGRATION_1_2)
            .build()

        newsRepository = NewsRepository(database.newsDao(), RetrofitInstance.api)
    }


//    fun getNewsDao(): NewsDao {
//        return database.newsDao()
//    }

    fun getRepository(): NewsRepository {
        return newsRepository
    }
}
