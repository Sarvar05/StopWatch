package com.example.myapplication.news.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Entity(tableName = "news")
data class NewsArticle(
    @PrimaryKey val url: String,
    val title: String,
    val description: String? = null,
    val urlToImage: String? = null,
    val publishedAt: String? = null,
    val content: String? = null,
    val category: String? = null,
    var isFavorite: Boolean = false
)

val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            "CREATE TABLE news_new (" +
                    "url TEXT NOT NULL PRIMARY KEY, " +
                    "title TEXT NOT NULL, " +
                    "description TEXT, " +
                    "urlToImage TEXT, " +
                    "publishedAt TEXT, " +
                    "content TEXT, " +
                    "category TEXT, " +
                    "isFavorite INTEGER NOT NULL DEFAULT 0)"
        )

        database.execSQL(
            "INSERT OR IGNORE INTO news_new (url, title, description, urlToImage, publishedAt, content, category, isFavorite) " +
                    "SELECT url, title, description, urlToImage, publishedAt, content, category, isFavorite FROM news"
        )

        database.execSQL("DROP TABLE news")
        database.execSQL("ALTER TABLE news_new RENAME TO news")
    }
}
