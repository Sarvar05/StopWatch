package com.example.myapplication.news.screens

import android.webkit.WebView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun ArticleScreen(articleUrl: String) {
    val context = LocalContext.current
    val webView = remember { WebView(context) }

    webView.settings.javaScriptEnabled = true
    webView.settings.domStorageEnabled = true

    LaunchedEffect(articleUrl) {
        webView.loadUrl(articleUrl)
    }

    AndroidView(
        factory = {
            webView
        },
        modifier = Modifier.fillMaxSize()
    )
}
