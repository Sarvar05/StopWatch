package com.example.weather.presentation

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weather.R
import com.example.weather.sealed.WeatherState

@Composable
fun WeatherScreen(viewModel: WeatherViewModel = viewModel()) {
    val weatherState by viewModel.weatherState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchWeather()
    }

    when (weatherState) {
        is WeatherState.Loading -> {
            LoadingIndicator()
        }
        is WeatherState.Success -> {
            val data = weatherState as WeatherState.Success
            WeatherDetails(data, viewModel::fetchWeather)
        }
        is WeatherState.Error -> {
            val error = weatherState as WeatherState.Error
            WeatherDetails(
                WeatherState.Success(0f, 0, 0f, "No data"),
                viewModel::fetchWeather,
                error.message
            )
        }
    }
}

@Composable
fun WeatherDetails(data: WeatherState.Success, onRefresh: () -> Unit, errorMessage: String? = null) {
    val iconRes = getWeatherIcon(data.description)

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

        Spacer(modifier = Modifier.height(24.dp))

        Image(
            painter = painterResource(id = iconRes),
            contentDescription = null,
            modifier = Modifier
                .size(120.dp)
                .padding(8.dp)
        )

        Text(
            text = if (errorMessage == null) "${data.temperature}°C" else "0°C",
            fontSize = 32.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Black
        )

        Text(
            text = if (errorMessage == null) data.description else "No data",
            fontSize = 20.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(16.dp))

        WeatherDetailsCard(data.windSpeed, data.humidity)

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = onRefresh) {
            Text(text = stringResource(id = R.string.updated))
        }

        errorMessage?.let {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = it,
                fontSize = 16.sp,
                color = Color.Red
            )
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
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(id = R.string.wind),
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                Text(text = "$wind м/с", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
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

@Composable
fun LoadingIndicator() {
    CircularProgressIndicator(modifier = Modifier.padding(16.dp))
}

@DrawableRes
fun getWeatherIcon(description: String): Int {
    return when {
        description.contains("clear", ignoreCase = true) -> R.drawable.sun_img
        description.contains("cloud", ignoreCase = true) -> R.drawable.ic_cloudy
        description.contains("rain", ignoreCase = true) -> R.drawable.rain_img
        else -> R.drawable.sun_img
    }
}

@Preview(showBackground = true)
@Composable
fun WeatherScreenPreview() {
    WeatherScreen()
}
