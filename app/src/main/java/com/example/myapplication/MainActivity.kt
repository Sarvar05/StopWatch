package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.drawer.SetupNavigation
import com.example.myapplication.news.data.DatabaseProvider
import com.example.myapplication.news.navigation.SetupNav
import com.example.myapplication.news.viewmodel.NewsApp
import com.example.myapplication.news.viewmodel.NewsViewModel
import com.example.myapplication.weather_app.ui.theme.MyApplicationTheme
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DatabaseProvider.init(this)
            MyApplicationTheme {
                SetupNav()
            }
        }
    }
}