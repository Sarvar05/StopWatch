package com.example.myapplication.stop_watch

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.R


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun StopWatchScreen(viewModel: WatchViewModel) {
    val seconds by viewModel.seconds.collectAsState()
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

            Box(
                modifier = Modifier.size(200.dp)
            ) {

                Text(
                    text = formattedTime,
                    color = Color.Black,
                    fontSize = 44.sp,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            Row(
                modifier = Modifier.padding(top = 32.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    onClick = {
                        viewModel.resetTimer()
                        viewModel.stopTimer()
                    },
                    modifier = Modifier.size(80.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.clear_button),
                        contentDescription = "Clear",
                        modifier = Modifier.fillMaxSize()
                    )
                }

                Spacer(modifier = Modifier.width(40.dp))

                IconButton(
                    onClick = {
                        if (viewModel.isRunning) {
                            viewModel.stopTimer()
                        } else {
                            viewModel.resumeTimer()
                        }
                    },
                    modifier = Modifier.size(80.dp)
                ) {
                    Icon(
                        painter = painterResource(id = if (viewModel.isRunning) R.drawable.pause_button else R.drawable.play_button),
                        contentDescription = if (viewModel.isRunning) "Pause" else "Play",
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}
