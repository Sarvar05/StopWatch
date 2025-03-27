package com.example.myapplication.news.viewmodel

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.news.data.NewsArticle
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.collectAsState
import coil.compose.rememberImagePainter
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import coil.compose.rememberAsyncImagePainter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import androidx.core.net.toUri
import coil.compose.rememberAsyncImagePainter


@Composable
fun NewsApp(viewModel: NewsViewModel = NewsViewModel()) {
    val newsList = viewModel.newsList.collectAsState(initial = emptyList()).value
    val isLoading = viewModel.isLoading.collectAsState(initial = true).value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Latest News",
            style = typography.headlineLarge,
            color = Color.Black,
            modifier = Modifier.padding(8.dp)
        )

        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.fillMaxSize().wrapContentSize(Alignment.Center))
        } else {
            NewsList(newsList = newsList)
        }
    }
}


@Composable
fun NewsList(newsList: List<NewsArticle>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
    ) {
        items(newsList) { article ->
            NewsItem(article = article)
        }
    }
}

@Composable
fun NewsItem(article: NewsArticle) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {

            if (article.urlToImage != null) {
                Image(
                    painter = rememberAsyncImagePainter(article.urlToImage),
                    contentDescription = article.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = article.title,
                style = typography.headlineLarge
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = article.description ?: "No description",
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(onClick = {
                article.url?.let { url ->
                    //OpenArticle(url)
                }
            }) {

            }
        }
    }
}

@Composable
fun OpenArticle(url: String) {
    val context = LocalContext.current
    val uri = Uri.parse(url)
    val intent = Intent(Intent.ACTION_VIEW, uri)
    context.startActivity(intent)
}
