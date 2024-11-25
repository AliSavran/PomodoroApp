package com.alisavran.pomodoroapp

import android.content.Context
import android.media.MediaPlayer
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.toDuration

class PomodoroViewModel : ViewModel() {

    private var job: Job? = null
    private var timerDuration: Duration = 25.toDuration(DurationUnit.MINUTES)
    private var breakDuration: Duration = 5.toDuration(DurationUnit.MINUTES)
    private var isWorkTime = true

    private val _timeLeft = mutableStateOf(timerDuration)
    val timeLeft: State<Duration> = _timeLeft

    private val _isRunning = mutableStateOf(false)
    val isRunning: State<Boolean> = _isRunning

    private val _isWorkSession = mutableStateOf(true)
    val isWorkSession: Boolean
        get() = _isWorkSession.value

    private var mediaPlayer: MediaPlayer? = null

    fun initializeMediaPlayer(context: Context) {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(context, R.raw.work_music)
        }
    }


    fun startTimer() {
        _isRunning.value = true
        job = CoroutineScope(Dispatchers.Main).launch {
            while (_timeLeft.value > 0.toDuration(DurationUnit.SECONDS)) {
                delay(1000)
                _timeLeft.value = _timeLeft.value - 1.toDuration(DurationUnit.SECONDS)
            }
            switchToBreak()
        }
        startMusic()
    }

    fun stopTimer() {
        _isRunning.value = false
        job?.cancel()
        stopMusic()
    }

    fun resetTimer() {
        _isRunning.value = false
        job?.cancel()
        _timeLeft.value = timerDuration
        stopMusic()
    }

    fun switchToBreak() {
        isWorkTime = !isWorkTime
        _isWorkSession.value = isWorkTime
        _timeLeft.value = if (isWorkTime) timerDuration else breakDuration
        if (isWorkTime) {
            startMusic()
        } else {
            stopMusic()
        }
    }

    private fun startMusic() {
        mediaPlayer?.start()
        mediaPlayer?.isLooping = true
    }

    private fun stopMusic() {
        mediaPlayer?.pause()
        mediaPlayer?.seekTo(0)
    }

    override fun onCleared() {
        super.onCleared()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}