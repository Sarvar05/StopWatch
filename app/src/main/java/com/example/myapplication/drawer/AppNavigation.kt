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
import com.example.myapplication.news.screens.ArticleScreen
import com.example.myapplication.news.screens.NewCategoryScreen
import com.example.myapplication.news.viewmodel.NewsApp
import com.example.myapplication.stop_watch.StopWatchScreen
import com.example.myapplication.stop_watch.WatchViewModel
import com.example.myapplication.weather_app.WeatherScreen

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
