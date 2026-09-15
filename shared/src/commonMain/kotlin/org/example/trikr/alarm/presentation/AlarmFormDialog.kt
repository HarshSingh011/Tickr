package org.example.trikr.alarm.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

@Composable
fun AlarmFormDialog(
    uiState: AlarmUiState,
    onTitleChange: (String) -> Unit,
    onTimeChange: (Int, Int) -> Unit,
    onIsDailyChange: (Boolean) -> Unit,
    onFrequencyChange: (Int) -> Unit,
    onSave: () -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.95f)
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                Text(
                    text = "Set Task Alarm",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(24.dp))

                OutlinedTextField(
                    value = uiState.title,
                    onValueChange = onTitleChange,
                    label = { Text("Task Title (e.g. Gym)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface
                    )
                )
                Spacer(modifier = Modifier.height(16.dp))

                Text("Time", fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurface)
                Spacer(modifier = Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = uiState.timeH.toString(),
                        onValueChange = { onTimeChange(it.toIntOrNull() ?: 0, uiState.timeM) },
                        label = { Text("Hour (0-23)") },
                        modifier = Modifier.weight(1f),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.surface,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surface
                        )
                    )
                    OutlinedTextField(
                        value = uiState.timeM.toString(),
                        onValueChange = { onTimeChange(uiState.timeH, it.toIntOrNull() ?: 0) },
                        label = { Text("Min (0-59)") },
                        modifier = Modifier.weight(1f),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.surface,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surface
                        )
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))

                Text("Repeat", fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurface)
                Spacer(modifier = Modifier.height(8.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    FilterChip(
                        selected = uiState.isDaily,
                        onClick = { onIsDailyChange(true) },
                        label = { Text("Daily") },
                        leadingIcon = if (uiState.isDaily) { { Icon(Icons.Default.Check, null) } } else null
                    )
                    FilterChip(
                        selected = !uiState.isDaily,
                        onClick = { onIsDailyChange(false) },
                        label = { Text("Just Today") },
                        leadingIcon = if (!uiState.isDaily) { { Icon(Icons.Default.Check, null) } } else null
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))

                Text("Snooze: Every ${uiState.frequencyMinutes} mins", fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurface)
                Slider(
                    value = uiState.frequencyMinutes.toFloat(),
                    onValueChange = { onFrequencyChange(it.toInt()) },
                    valueRange = 1f..60f,
                    steps = 59
                )
                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = onSave,
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Save", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
