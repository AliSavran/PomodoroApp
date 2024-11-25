package com.alisavran.pomodoroapp.ui.theme

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.alisavran.pomodoroapp.PomodoroViewModel
import kotlin.time.DurationUnit
import kotlin.time.toDuration

@Composable
fun PomodoroScreen(pomodoroViewModel: PomodoroViewModel = viewModel()) {
    val timeLeft = pomodoroViewModel.timeLeft.value
    val isRunning = pomodoroViewModel.isRunning.value
    val isWorkSession = pomodoroViewModel.isWorkSession

    val minutes = timeLeft.inWholeMinutes
    val seconds = timeLeft.minus(minutes.toDuration(DurationUnit.MINUTES)).inWholeSeconds

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        pomodoroViewModel.initializeMediaPlayer(context)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Title
        Text(
            text = if (isWorkSession) "Working Time" else "Break Time",
            style = MaterialTheme.typography.bodyLarge,
            fontSize = 48.sp,
            color = if (isWorkSession) Color.Blue else Color.Red
        )
        // Timer
        Text(
            text = String.format("%02d:%02d", minutes, seconds),
            style = MaterialTheme.typography.bodyMedium,
            fontSize = 24.sp,
            modifier = Modifier.padding(vertical = 32.dp)
        )
        // Start/Stop Button
        Button(
            onClick = {
                if (isRunning) {
                    pomodoroViewModel.stopTimer()
                } else {
                    pomodoroViewModel.startTimer()
                }
            }
        ) {
            Text(text = if (isRunning) "Pause" else "Start")
        }

        // Reset Button
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                pomodoroViewModel.resetTimer()
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
        ) {
            Text(text = "Reset")
        }
    }
}
