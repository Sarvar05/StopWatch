package com.example.news.utils


import com.example.news.data.exception.ApiException
import com.example.news.data.exception.CategoryException
import com.example.news.data.repository.NewsRepository
import retrofit2.HttpException
import java.io.IOException

fun Throwable.toCategoryError(category: String): CategoryException {
    return when (this) {
        is CategoryException -> this
        else -> CategoryException(category, this.message ?: "Unknown error", this)
    }
}

fun Throwable.toErrorMessage(repository: NewsRepository): String {
    return when (this) {
        is IOException -> "No internet connection"
        is HttpException -> repository.getHttpErrorMessage(this.code())
        is ApiException -> "API Error: ${this.message} (Code: ${this.statusCode})"
        is CategoryException -> this.message ?: "Error in category ${this.category}"
        else -> "Unexpected error: ${this.localizedMessage}"
    }
}


