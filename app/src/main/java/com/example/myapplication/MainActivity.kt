package com.example.myapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: WatchViewModel
    private var isPaused = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[WatchViewModel::class.java]

        lifecycleScope.launch {
            viewModel.seconds.collect { time ->
                binding.timerText.text = viewModel.formatTime(time)
            }
        }

        binding.clear.setOnClickListener {
            viewModel.resetTimer()
            isPaused = true
            updatePlayPauseIcon()
        }

        binding.playPauseButton.setOnClickListener {
            toggleTimer()
        }
    }

    override fun onStop() {
        super.onStop()
        viewModel.stopTimer()
    }

    override fun onResume() {
        super.onResume()
        viewModel.resumeTimer()
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
        val iconRes = if (isPaused) R.drawable.play_button else R.drawable.pause_button
        binding.playPauseButton.setImageResource(iconRes)
    }
}
