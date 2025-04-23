package com.example.news.data.exception

class ApiException(val statusCode: Int, message: String) : Exception(message)
