package com.example.myapplication.drawer
import android.os.Build
import android.view.LayoutInflater
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.myapplication.R
import com.example.myapplication.news.viewmodel.NewsApp
import com.example.myapplication.stop_watch.StopWatch
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
    }
}
