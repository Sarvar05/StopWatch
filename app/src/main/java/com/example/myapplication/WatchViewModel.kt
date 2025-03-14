package com.example.myapplication

import android.app.Application
import android.content.Context
import android.os.SystemClock
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.isActive
import java.util.Locale

private val Context.dataStore by preferencesDataStore(name = "stopwatch_prefs")

class WatchViewModel(application: Application) : AndroidViewModel(application) {
    companion object {
        private val KEY_TIME = longPreferencesKey("time")
        private val KEY_RUNNING = booleanPreferencesKey("running")
        private val KEY_START_TIME = longPreferencesKey("start_time")
    }

    private val dataStore = application.dataStore
    private val _seconds = MutableStateFlow(0L)
    val seconds: StateFlow<Long> = _seconds.asStateFlow()

    private var job: Job? = null
    private var startTime = 0L
    var isRunning = false

    init {
        viewModelScope.launch {
            val prefs = dataStore.data.first()
            val savedTime = prefs[KEY_TIME] ?: 0L
            val wasRunning = prefs[KEY_RUNNING] == true

            _seconds.value = savedTime
            isRunning = wasRunning
        }
    }


    private fun startTimer() {
        if (job?.isActive == true) return
        isRunning = true
        startTime = SystemClock.elapsedRealtime() - (_seconds.value * 1000)

        job = viewModelScope.launch(Dispatchers.IO) {
            while (isActive) {
                val newTime = (SystemClock.elapsedRealtime() - startTime) / 1000
                _seconds.value = newTime
                delay(1000)
            }
        }
    }

    fun formatTime(seconds: Long): String {
        val hours = seconds / 3600
        val minutes = (seconds % 3600) / 60
        val sec = seconds % 60
        return String.format(Locale.US, "%02d:%02d:%02d", hours, minutes, sec)
    }

    fun resetTimer() {
        job?.cancel()
        _seconds.value = 0L
        startTime = SystemClock.elapsedRealtime()
        startTimer()
        saveState()
    }

    fun stopTimer() {
        job?.cancel()
        isRunning = false
        saveState()
    }

    fun resumeTimer() {
        if (!isRunning) {
            startTimer()
        }
    }

    private fun saveState() {
        viewModelScope.launch {
            dataStore.edit { prefs ->
                prefs[KEY_TIME] = _seconds.value
                prefs[KEY_RUNNING] = isRunning
                prefs[KEY_START_TIME] = SystemClock.elapsedRealtime()
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
        saveState()
    }
}