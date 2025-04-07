package com.example.myapplication.news.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Entity(tableName = "news")
data class NewsArticle(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val description: String? = null,
    val urlToImage: String? = null,
    val publishedAt: String? = null,
    val url: String? = null,
    val content: String? = null,
    val category: String? = null,
    var isFavorite: Boolean = false
)


val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            "CREATE TABLE news_new (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    "title TEXT NOT NULL, " +
                    "description TEXT, " +
                    "urlToImage TEXT, " +
                    "publishedAt TEXT, " +
                    "url TEXT, " +
                    "content TEXT, " +
                    "category TEXT, " +
                    "isFavorite INTEGER NOT NULL DEFAULT 0)"
        )

        database.execSQL("INSERT INTO news_new (title, description, urlToImage, publishedAt, url, content, category, isFavorite) SELECT title, description, urlToImage, publishedAt, url, content, category, isFavorite FROM news")

        database.execSQL("DROP TABLE news")

        database.execSQL("ALTER TABLE news_new RENAME TO news")
    }
}
