package com.example.myapplication

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import com.example.myapplication.drawer.SetupNavigation
import com.example.news.data.local.DatabaseProvider
import com.example.weather.presentation.ui.theme.MyApplicationTheme
import dagger.hilt.android.HiltAndroidApp
@RequiresApi(Build.VERSION_CODES.O)
@HiltAndroidApp
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DatabaseProvider.init(this)
            MyApplicationTheme {
                SetupNavigation()
            }
        }
    }
}