package com.example.news.presentation.article

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.news.presentation.NewsList
import com.example.news.presentation.NewsViewModel


@Composable
fun NewCategoryScreen(category: String, navController: NavController) {
    val viewModel: NewsViewModel = viewModel()
    val newsList = viewModel.newsList.collectAsState(initial = emptyList()).value
    val isLoading = viewModel.isLoading.collectAsState(initial = true).value

    LaunchedEffect(category) {
        viewModel.getNewsByCategory(category)
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {


        Text(
            text = "$category News",
            style = MaterialTheme.typography.headlineLarge,
            color = Color.Black,
            modifier = Modifier.padding(8.dp)
        )

        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(Alignment.Center)
            )
        } else {
            NewsList(newsList = newsList, navController = navController, viewModel = viewModel)
        }

    }
}
