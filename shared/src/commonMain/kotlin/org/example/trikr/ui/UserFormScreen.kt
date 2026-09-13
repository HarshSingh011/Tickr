package org.example.trikr.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.example.trikr.theme.TickrTheme
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.clickable

import org.example.trikr.ui.model.TaskEntry
import org.example.trikr.ui.components.TaskItemCard

@Composable
fun UserFormScreen(
    onComplete: () -> Unit = {}
) {
    var criteria by remember { mutableStateOf("") }
    var tasks by remember { mutableStateOf(listOf(TaskEntry(id = 0, name = "", durationMinutes = 60))) }
    var nextId by remember { mutableStateOf(1) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "Your Tasks",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 8.dp, top = 48.dp)
        )
        
        Text(
            text = "Add tasks and the duration you want to dedicate to each.",
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
            modifier = Modifier.padding(bottom = 32.dp)
        )
        
        Text(
            text = "Task List",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(bottom = 12.dp)
        )
        

        tasks.forEach { task ->
            TaskItemCard(
                task = task,
                onNameChange = { newName ->
                    tasks = tasks.map { 
                        if (it.id == task.id) it.copy(name = newName) else it 
                    }
                },
                onDurationChange = { newDuration ->
                    tasks = tasks.map { 
                        if (it.id == task.id) it.copy(durationMinutes = newDuration) else it 
                    }
                },
                onDelete = {
                    tasks = tasks.filter { it.id != task.id }
                },
                showDelete = tasks.size > 1
            )
        }
        
        Spacer(modifier = Modifier.height(8.dp))


        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedButton(
                onClick = { 
                    tasks = tasks + TaskEntry(id = nextId++, name = "", durationMinutes = 60)
                },
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Task")
                Spacer(Modifier.width(8.dp))
                Text("Add Task")
            }

            Button(
                onClick = onComplete,
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text(
                    text = "Submit",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
        
        Spacer(modifier = Modifier.height(32.dp))
    }
}

fun formatDuration(minutes: Int): String {
    val h = minutes / 60
    val m = minutes % 60
    return if (h > 0 && m > 0) "${h}h ${m}m"
    else if (h > 0) "${h}h"
    else "${m}m"
}

@Preview
@Composable
fun UserFormScreenPreview() {
    TickrTheme {
        UserFormScreen()
    }
}

@Preview
@Composable
fun ManualDurationDialogPreview() {
    TickrTheme {
        ManualDurationDialogContent(
            initialMinutes = 90,
            onDismiss = {},
            onSave = {}
        )
    }
}
