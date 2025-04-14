package com.example.myapplication.stop_watch

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
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.util.Locale

private val Context.dataStore by preferencesDataStore(name = "stopwatch_prefs")

class WatchViewModel(application: Application) : AndroidViewModel(application) {
    companion object {
        private val KEY_TIME = longPreferencesKey("time")
        private val KEY_RUNNING = booleanPreferencesKey("running")
    }

    private val dataStore = application.dataStore

    private val _seconds = MutableStateFlow(0L)
    val seconds: StateFlow<Long> = _seconds.asStateFlow()

    private val _isRunning = MutableStateFlow(false)
    val isRunning: StateFlow<Boolean> = _isRunning.asStateFlow()

    private var job: Job? = null
    private var startTime = 0L

    init {
        viewModelScope.launch {
            val prefs = dataStore.data.first()
            val savedTime = prefs[KEY_TIME] ?: 0L
            val wasRunning = prefs[KEY_RUNNING] == true

            _seconds.value = savedTime
            _isRunning.value = wasRunning

            if (wasRunning) {
                startTimer(savedTime)
            }
        }
    }

    private fun startTimer(savedTime: Long) {
        startTime = SystemClock.elapsedRealtime() - (savedTime * 1000)
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
        saveState()
    }

    fun stopTimer() {
        job?.cancel()
        _isRunning.value = false
        saveState()
    }

    fun resumeTimer() {
        if (!_isRunning.value) {
            startTimer(_seconds.value)
            _isRunning.value = true
        }
    }

    private fun saveState() {
        viewModelScope.launch {
            dataStore.edit { prefs ->
                prefs[KEY_TIME] = _seconds.value
                prefs[KEY_RUNNING] = _isRunning.value
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
        saveState()
    }
}
