package org.example.trikr.alarm.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.example.trikr.alarm.domain.model.Alarm
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp)
    ) {
        Text(
            text = "Set Task Alarm",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(24.dp))

        // Title Input
        OutlinedTextField(
            value = uiState.title,
            onValueChange = { viewModel.updateTitle(it) },
            label = { Text("Task Title (e.g. Gym)") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Custom Time Picker Row
        Text("Time", fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onBackground)
        Spacer(modifier = Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = uiState.timeH.toString(),
                onValueChange = { viewModel.updateTime(it.toIntOrNull() ?: 0, uiState.timeM) },
                label = { Text("Hour (0-23)") },
                modifier = Modifier.weight(1f)
            )
            OutlinedTextField(
                value = uiState.timeM.toString(),
                onValueChange = { viewModel.updateTime(uiState.timeH, it.toIntOrNull() ?: 0) },
                label = { Text("Minute (0-59)") },
                modifier = Modifier.weight(1f)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Repeat Options (Daily vs Just Today)
        Text("Repeat", fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onBackground)
        Spacer(modifier = Modifier.height(8.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            FilterChip(
                selected = uiState.isDaily,
                onClick = { viewModel.updateIsDaily(true) },
                label = { Text("Daily") },
                leadingIcon = if (uiState.isDaily) { { Icon(Icons.Default.Check, null) } } else null
            )
            FilterChip(
                selected = !uiState.isDaily,
                onClick = { viewModel.updateIsDaily(false) },
                label = { Text("Just Today") },
                leadingIcon = if (!uiState.isDaily) { { Icon(Icons.Default.Check, null) } } else null
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Frequency Options
        Text("Snooze/Frequency: Every ${uiState.frequencyMinutes} mins", fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onBackground)
        Slider(
            value = uiState.frequencyMinutes.toFloat(),
            onValueChange = { viewModel.updateFrequency(it.toInt()) },
            valueRange = 1f..60f,
            steps = 59
        )
        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { viewModel.saveAlarm() },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text("Save Alarm", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(32.dp))
        Text("Saved Alarms", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(alarms) { alarm ->
                AlarmItemCard(alarm)
            }
            item { Spacer(modifier = Modifier.height(150.dp)) }
        }
    }
}


