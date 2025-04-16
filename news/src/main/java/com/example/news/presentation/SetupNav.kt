package com.example.news.presentation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument



@Composable
fun SetupNav() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "news_list") {
        composable("news_list") {
            NewsApp(navController = navController)
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