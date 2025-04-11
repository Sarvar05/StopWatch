package com.example.myapplication.stop_watch

import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.R
import com.example.myapplication.databinding.StopWatchBinding
import kotlinx.coroutines.launch

class StopWatch : AppCompatActivity(R.layout.stop_watch) {
    private lateinit var binding: StopWatchBinding
    private lateinit var viewModel: WatchViewModel
    private var isPaused = true

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = StopWatchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setContent {
            StopWatchScreen(viewModel)
        }

        viewModel = ViewModelProvider(this)[WatchViewModel::class.java]

        lifecycleScope.launch {
            viewModel.seconds.collect { time ->
                binding.timerText.text = viewModel.formatTime(time)
            }
        }

        binding.clear.setOnClickListener {
            viewModel.resetTimer()
            isPaused = true
            viewModel.stopTimer()
            updatePlayPauseIcon()
        }

        binding.playPauseButton.setOnClickListener {
            toggleTimer()
        }
    }


    override fun onResume() {
        super.onResume()

        if (!isPaused) {
            viewModel.resumeTimer()
        }
    }

    private fun toggleTimer() {
        isPaused = !isPaused
        updatePlayPauseIcon()

        lifecycleScope.launch {
            if (isPaused) {
                viewModel.stopTimer()
            } else {
                viewModel.resumeTimer()
            }
        }
    }

    private fun updatePlayPauseIcon() {
        val iconRes = if (isPaused) R.drawable.play else R.drawable.pause
        binding.playPauseButton.setImageResource(iconRes)
    }
}