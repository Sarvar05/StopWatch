package com.example.news.data.exception

class CategoryException(
    val category: String,
    message: String,
    cause: Throwable? = null
) : Exception("[$category] $message", cause)