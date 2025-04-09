package com.example.myapplication.news.viewmodel

import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.bumptech.glide.Glide
import com.example.myapplication.R
import com.example.myapplication.news.data.DatabaseProvider
import com.example.myapplication.weather_app.ui.theme.darkBlue

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsApp(
    navController: NavController
) {
    val repository = DatabaseProvider.getRepository()
    val viewModel: NewsViewModel = viewModel(factory = NewsViewModelFactory(repository))

    val categories = listOf("All", "Sports", "Business", "Technology", "Health", "Favorites")
    val selectedCategory = rememberSaveable { mutableStateOf("All") }

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
            .background(MaterialTheme.colorScheme.background)
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
            .padding(5.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {

            Row(
                modifier = Modifier.padding(15.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = "News",
                    style = TextStyle(fontSize = 25.sp),
                    color = MaterialTheme.colorScheme.onBackground
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
                            containerColor = if (selectedCategory.value == category)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.surface,
                            contentColor = if (selectedCategory.value == category)
                                MaterialTheme.colorScheme.onPrimary
                            else
                                MaterialTheme.colorScheme.onSurface
                        )
                    ) {
                        Text(text = category)
                    }

                }
            }

            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .wrapContentSize(Alignment.Center),
                    color = MaterialTheme.colorScheme.primary
                )
            } else {

                NewsList(
                    newsList = displayedNews,
                    navController = navController,
                    viewModel = viewModel,
                )
            }
        }
    }
}

@Composable
fun NewsList(
    newsList: List<NewsArticle>,
    navController: NavController,
    viewModel: NewsViewModel,

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

    @Composable
    fun GlideImage(url: String?, modifier: Modifier = Modifier) {
        AndroidView(
            factory = { context ->
                ImageView(context).apply {scaleType
                    scaleType = ImageView.ScaleType.CENTER_CROP
                }
            },
            modifier = modifier,
            update = { imageView ->
                Glide.with(imageView.context)
                    .load(url)
                    .error(R.drawable.no_image1)
                    .into(imageView)
            }
        )
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline,
                shape = RoundedCornerShape(12.dp)
            ),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface)
                .padding(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(12.dp))
            ) {
                if (article.urlToImage.isNullOrEmpty()) {
                    Image(
                        painter = painterResource(R.drawable.no_image1),
                        contentDescription = "Your image",
                        modifier = Modifier
                    )

                } else {
                    GlideImage(
                        url = article.urlToImage,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = article.title,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 2,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = article.description ?: "",
                style = MaterialTheme.typography.bodySmall,
                maxLines = 3,
                color = MaterialTheme.colorScheme.onSurfaceVariant
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
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text("Read Full Article")
                }

                IconButton(
                    onClick = {
                        Log.d("Favorite", "Icon button clicked for article: ${article.title}")
                        viewModel.toggleFavorite(article)

                    }
                ) {
                    Icon(
                        imageVector = if (article.isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = "Favorite",
                        tint = if (article.isFavorite)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.outline,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        }
    }
}