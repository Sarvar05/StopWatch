package com.example.myapplication.weather_app

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.ui.res.stringResource
import com.example.myapplication.R
import java.util.*

@Composable
fun WeatherScreen() {
    val coroutineScope = rememberCoroutineScope()
    var temperature by remember { mutableFloatStateOf(0f) }
    var humidity by remember { mutableIntStateOf(0) }
    var windSpeed by remember { mutableFloatStateOf(0f) }
    var description by remember { mutableStateOf("${R.string.loading}") }
    var lastUpdated by remember { mutableStateOf<String?>(null) }

    fun fetchWeather() {
        coroutineScope.launch {
            try {
                val response = RetrofitInstance.api.getWeather("Tashkent", API_KEY_WEATHER)

                temperature = response.main.temp
                humidity = response.main.humidity
                windSpeed = response.wind.speed
                description =
                    response.weather.firstOrNull()?.description ?: R.string.unknown.toString()

                val currentTime = System.currentTimeMillis()

                val sdf = SimpleDateFormat("dd/MM/yyyy || HH:mm:ss", Locale.getDefault())
                val formattedDate = sdf.format(Date(currentTime))

                lastUpdated = "${R.string.updated}: $formattedDate"
            } catch (e: Exception) {
                description = " ${R.string.error} : ${e.message}"
            }
        }
    }

    LaunchedEffect(Unit) {
        fetchWeather()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F8F8))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.weather_title),
            fontSize = 24.sp,
            color = Color.DarkGray
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "$temperature°C", fontSize = 48.sp, fontWeight = FontWeight.Bold)
        Text(text = description, fontSize = 20.sp, color = Color.Gray)
        Spacer(modifier = Modifier.height(16.dp))

        WeatherDetailsCard(windSpeed, humidity)

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { fetchWeather() }) {
            Text(text = stringResource(id = R.string.updated))
        }

        if (lastUpdated != null) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = lastUpdated ?: "", fontSize = 14.sp, color = Color.Gray)
        }
    }
}

@Composable
fun WeatherDetailsCard(wind: Float, humidity: Int) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = stringResource(id = R.string.wind),
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                Text(text = "$wind м/с", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = stringResource(id = R.string.humidity),
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                Text(text = "$humidity%", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WeatherScreenPreview() {
    WeatherScreen()
}
