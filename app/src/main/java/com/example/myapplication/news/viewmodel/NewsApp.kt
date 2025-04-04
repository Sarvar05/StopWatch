package com.example.myapplication.news.viewmodel


import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.myapplication.news.data.NewsArticle
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import androidx.navigation.NavController
import com.example.myapplication.weather_app.ui.theme.customColor
import com.example.myapplication.weather_app.ui.theme.darkBlue


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsApp(viewModel: NewsViewModel = viewModel(), navController: NavController) {

    val categories = listOf("All", "Sports", "Business", "Technology", "Health", "Favorites")
    val selectedCategory = remember { mutableStateOf("All") }

    val newsList by viewModel.newsList.collectAsState(initial = emptyList())
    val favoriteNewsList by viewModel.favoriteNewsList.collectAsState(initial = emptyList())
    val isLoading by viewModel.isLoading.collectAsState(initial = true)

    LaunchedEffect(selectedCategory.value) {
        if (selectedCategory.value != "Favorites") {
            viewModel.getNewsByCategory(selectedCategory.value)
        }


    }
    val displayedNews = if (selectedCategory.value == "Favorites") {
        favoriteNewsList
    } else {
        newsList
    }


    val state = rememberPullToRefreshState()
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = customColor)
    ) {}
    PullToRefreshBox(
        isRefreshing = isLoading,
        onRefresh = {
            if (selectedCategory.value != "Favorites") {
                viewModel.getNewsByCategory(selectedCategory.value)
            }
        },
        state = state,
        indicator = {
            Indicator(
                modifier = Modifier.align(Alignment.TopCenter),
                isRefreshing = isLoading,
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                state = state
            )
        },
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {

            Row(
                modifier = Modifier
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = "Latest News"
                )
            }

            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(categories) { category ->
                    Button(
                        onClick = { selectedCategory.value = category },
                        modifier = Modifier.height(40.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (selectedCategory.value == category) Color(
                                0xFF003B5C
                            ) else Color(0xFFA0D2EB),
                            contentColor = Color.White
                        )
                    ) {
                        Text(text = category)
                    }
                }
            }

            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .wrapContentSize(Alignment.Center)
                )
            } else {

                NewsList(
                    newsList = displayedNews,
                    navController = navController,
                    viewModel = viewModel
                )
            }
        }
    }
}


@Composable
fun NewsList(
    newsList: List<NewsArticle>,
    navController: NavController,
    viewModel: NewsViewModel
) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(newsList) { article ->
            NewsItem(article = article, navController = navController, viewModel = viewModel)
        }
    }
}

@Composable
fun NewsItem(article: NewsArticle, navController: NavController, viewModel: NewsViewModel) {
    var isFavorite by remember { mutableStateOf(article.isFavorite) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .background(color = customColor)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {

            if (article.urlToImage != null) {
                Image(
                    painter = rememberAsyncImagePainter(article.urlToImage),
                    contentDescription = article.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = article.title,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = article.description ?: "No description",
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Button(
                    onClick = {
                        val encodedUrl = Uri.encode(article.url)
                        navController.navigate("article_screen/$encodedUrl")
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = darkBlue,
                        contentColor = Color.White
                    )
                ) {
                    Text("Read Full Article")
                }


                IconButton(
                    onClick = {
                        viewModel.toggleFavorite(article)
                        isFavorite = !isFavorite
                    }
                ) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = "Favorite",
                        tint = if (isFavorite) Color.Red else Color.Gray,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        }
    }
}
