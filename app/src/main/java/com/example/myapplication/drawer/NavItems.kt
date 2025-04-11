package com.example.myapplication.drawer

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.MailOutline
import androidx.compose.ui.graphics.vector.ImageVector

enum class Screens(val route: String) {
    WeatherScreen("weather_screen"),
    Stopwatch("stop_watch "),
    NewsApp("news_app")
}

data class NavItems(
    val title: String,
    val unselectedIcon: ImageVector,
    val selectedIcon: ImageVector,
    val route: String
)

val listOfNavItems = listOf(
    NavItems(
        title = "Weather",
        unselectedIcon = Icons.Outlined.Home,
        selectedIcon = Icons.Filled.Home,
        route = Screens.WeatherScreen.route
    ),
    NavItems(
        title = "Stopwatch",
        unselectedIcon = Icons.Outlined.MailOutline,
        selectedIcon = Icons.Filled.Email,
        route = Screens.Stopwatch.route
    ),
    NavItems(
        title = "News",
        unselectedIcon = Icons.Outlined.MailOutline,
        selectedIcon = Icons.Filled.Email,
        route = Screens.NewsApp.route
    )
)
