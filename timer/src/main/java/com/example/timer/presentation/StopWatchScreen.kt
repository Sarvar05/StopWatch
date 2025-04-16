package com.example.timer.presentation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun StopWatchScreen(viewModel:WatchViewModel) {
    val seconds by viewModel.seconds.collectAsState()
    val isRunning by viewModel.isRunning.collectAsState()
    val formattedTime = viewModel.formatTime(seconds)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Countdown Timer",
                color = Color.Black,
                fontSize = 24.sp,
                modifier = Modifier.padding(bottom = 36.dp)
            )

            Card(
                modifier = Modifier.size(220.dp),
                shape = RoundedCornerShape(24.dp),
                elevation = CardDefaults.cardElevation(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F0F0))
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = formattedTime,
                        color = Color.Black,
                        fontSize = 44.sp
                    )
                }
            }

            Row(
                modifier = Modifier.padding(top = 32.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = {
                        viewModel.resetTimer()
                        viewModel.stopTimer()
                    },
                    modifier = Modifier.size(width = 100.dp, height = 50.dp)
                ) {
                    Text("Reset")
                }

                Spacer(modifier = Modifier.width(40.dp))

                Button(
                    onClick = {
                        if (isRunning) {
                            viewModel.stopTimer()
                        } else {
                            viewModel.resumeTimer()
                        }
                    },
                    modifier = Modifier.size(width = 100.dp, height = 50.dp)
                ) {
                    Text(if (isRunning) "Pause" else "Play")
                }
            }
        }
    }
}
