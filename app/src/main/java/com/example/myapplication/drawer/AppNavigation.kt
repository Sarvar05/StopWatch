package com.example.myapplication.drawer

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.news.presentation.article.ArticleScreen
import com.example.news.presentation.article.NewCategoryScreen
import com.example.news.presentation.NewsApp
import com.example.timer.presentation.StopWatchScreen
import com.example.timer.presentation.WatchViewModel
import com.example.weather.presentation.WeatherScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavigation(
    navController: NavHostController,
    paddingValues: PaddingValues,
    viewModel: WatchViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Screens.WeatherScreen.route,
        modifier = Modifier.padding(paddingValues)
    ) {
        Screens.entries.forEach { screen ->
            composable(screen.route) {
                when (screen) {
                    Screens.WeatherScreen -> WeatherScreen()
                    Screens.Stopwatch -> StopWatchScreen(viewModel)
                    Screens.NewsApp -> NewsApp(navController)
                }
            }
        }

        composable(
            "category_screen/{category}",
            arguments = listOf(navArgument("category") { type = NavType.StringType })
        ) { backStackEntry ->
            val category = backStackEntry.arguments?.getString("category") ?: ""
            NewCategoryScreen(category = category, navController = navController)
        }

        composable(
            "article_screen/{articleUrl}",
            arguments = listOf(navArgument("articleUrl") { type = NavType.StringType })
        ) { backStackEntry ->
            val articleUrl = backStackEntry.arguments?.getString("articleUrl") ?: ""
            ArticleScreen(articleUrl = articleUrl)
        }
    }
}
