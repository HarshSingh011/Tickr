package org.example.trikr.alarm.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.example.trikr.di.ServiceLocator

@Composable
fun AlarmScreen(
    viewModel: AlarmViewModel = viewModel { 
        AlarmViewModel(
            ServiceLocator.getAlarmsUseCase,
            ServiceLocator.saveAlarmUseCase
        ) 
    }
) {
    val uiState by viewModel.uiState.collectAsState()
    val alarms by viewModel.alarms.collectAsState()
    var showDialog by remember { mutableStateOf(false) }

    // Unified premium gradient background (Blue fading to bottom)
    val backgroundBrush = Brush.verticalGradient(
        colors = listOf(
            MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
            MaterialTheme.colorScheme.background,
            MaterialTheme.colorScheme.background
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundBrush)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 48.dp, start = 24.dp, end = 24.dp)
        ) {
            Text(
                text = "Your Alarms",
                style = TextStyle(
                    brush = Brush.linearGradient(
                        colors = listOf(Color.White, Color.LightGray)
                    )
                ),
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Manage your task reminders and schedules",
                fontSize = 16.sp,
                color = Color.LightGray
            )
            Spacer(modifier = Modifier.height(32.dp))

            LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                items(alarms) { alarm ->
                    AlarmItemCard(alarm)
                }
                item { Spacer(modifier = Modifier.height(150.dp)) }
            }
        }

        FloatingActionButton(
            onClick = { showDialog = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 110.dp, end = 24.dp), // Lifted above glass nav
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            elevation = FloatingActionButtonDefaults.elevation(8.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Set Alarm")
        }

        if (showDialog) {
            AlarmFormDialog(
                uiState = uiState,
                onTitleChange = { viewModel.updateTitle(it) },
                onTimeChange = { h, m -> viewModel.updateTime(h, m) },
                onIsDailyChange = { viewModel.updateIsDaily(it) },
                onFrequencyChange = { viewModel.updateFrequency(it) },
                onSave = {
                    viewModel.saveAlarm()
                    showDialog = false
                },
                onDismiss = { showDialog = false }
            )
        }
    }
}

@androidx.compose.ui.tooling.preview.Preview
@Composable
fun AlarmScreenPreview() {
    org.example.trikr.theme.TickrTheme {
        AlarmScreen()
    }
}
