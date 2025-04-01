package com.example.myapplication.drawer
import android.view.LayoutInflater
import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.myapplication.R
import com.example.myapplication.news.viewmodel.NewsApp
import com.example.myapplication.stop_watch.StopWatch
import com.example.myapplication.weather_app.WeatherScreen

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screens.WeatherScreen.route) {
        Screens.entries.forEach { screen ->
            composable(screen.route) {
//                when (screen) {
//                    Screens.WeatherScreen -> WeatherScreen()
//                    Screens.Stopwatch -> Stopwatch()
//
//                }
            }
        }
    }
}@Composable
fun Stopwatch() {
    AndroidView(factory = { context ->
        LayoutInflater.from(context).inflate(R.layout.stop_watch, null)
    })
}
