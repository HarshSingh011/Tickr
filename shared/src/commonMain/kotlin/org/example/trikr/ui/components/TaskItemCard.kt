package org.example.trikr.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.example.trikr.ui.model.TaskEntry

@Composable
fun TaskItemCard(
    task: TaskEntry,
    onNameChange: (String) -> Unit,
    onDurationChange: (Int) -> Unit,
    onDelete: () -> Unit,
    showDelete: Boolean
) {
    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        ManualDurationDialog(
            initialMinutes = task.durationMinutes,
            onDismiss = { showDialog = false },
            onSave = { newMins ->
                onDurationChange(newMins)
                showDialog = false
            }
        )
    }

    Card(
        modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = task.name,
                    onValueChange = onNameChange,
                    label = { Text("Task Name") },
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
                if (showDelete) {
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(onClick = onDelete) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete Task", tint = MaterialTheme.colorScheme.error)
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            DurationSlider(
                title = "Duration",
                durationMinutes = task.durationMinutes,
                onValueChange = onDurationChange,
                onDurationClick = { showDialog = true }
            )
        }
    }
}
