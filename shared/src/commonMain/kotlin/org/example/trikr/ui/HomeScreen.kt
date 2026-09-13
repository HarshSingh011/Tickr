package org.example.trikr.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import org.example.trikr.theme.TickrTheme

data class ActiveTask(
    val id: Int,
    val name: String,
    val isCompleted: Boolean,
    val timeElapsedSeconds: Int
)

@Composable
fun HomeScreen() {
    var showAIPromptDialog by remember { mutableStateOf(false) }
    var secondsPassed by remember { mutableStateOf(10 * 3600 + 30 * 60) }
    
    LaunchedEffect(Unit) {
        while (true) {
            delay(1000)
            secondsPassed++
        }
    }

    val h = (secondsPassed / 3600) % 24
    val m = (secondsPassed % 3600) / 60
    val s = secondsPassed % 60
    
    val amPm = if (h >= 12) "PM" else "AM"
    val displayH = if (h % 12 == 0) 12 else h % 12
    val currentTime = "${displayH.toString().padStart(2, '0')}:${m.toString().padStart(2, '0')}:${s.toString().padStart(2, '0')} $amPm"

    val initialTasks = listOf(
        ActiveTask(1, "Morning Workout", isCompleted = true, timeElapsedSeconds = 5400),
        ActiveTask(2, "Deep Work: Project API", isCompleted = false, timeElapsedSeconds = 3600),
        ActiveTask(3, "Read a book", isCompleted = true, timeElapsedSeconds = 7200)
    )
    val tasks = remember { mutableStateListOf(*initialTasks.toTypedArray()) }
    val sortedTasks = tasks.sortedBy { it.isCompleted }
    val hasOngoingTask = tasks.any { !it.isCompleted }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAIPromptDialog = true },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(Icons.Default.AutoAwesome, contentDescription = "AI Prompt")
            }
        }
    ) { paddingValues ->
        if (showAIPromptDialog) {
            AIPromptDialog(
                onDismiss = { showAIPromptDialog = false },
                onSubmit = { _ -> showAIPromptDialog = false }
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues)
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                                MaterialTheme.colorScheme.background
                            )
                        )
                    )
                    .padding(top = 64.dp, bottom = 32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Current Time",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = currentTime,
                        fontSize = 48.sp,
                        fontWeight = FontWeight.Black,
                        color = MaterialTheme.colorScheme.primary,
                        letterSpacing = 2.sp
                    )
                }
            }

            AISuggestionCard()

            Text(
                text = "Today's Tasks",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp)
            )

            if (!hasOngoingTask) {
                Button(
                    onClick = {
                        tasks.add(ActiveTask(tasks.maxOfOrNull { it.id }?.plus(1) ?: 1, "New Focus Task", false, 0))
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Start Task")
                    Spacer(Modifier.width(8.dp))
                    Text("Start New Task", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(sortedTasks, key = { it.id }) { task ->
                    TaskStatusCard(
                        task = task,
                        onStop = { finalTime ->
                            val index = tasks.indexOfFirst { it.id == task.id }
                            if (index != -1) {
                                tasks[index] = task.copy(isCompleted = true, timeElapsedSeconds = finalTime)
                            }
                        }
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(80.dp))
                }
            }
        }
    }
}

@Composable
fun TaskStatusCard(task: ActiveTask, onStop: (Int) -> Unit) {
    var elapsedSeconds by remember { mutableStateOf(task.timeElapsedSeconds) }
    
    if (!task.isCompleted) {
        LaunchedEffect(task.id) {
            while (true) {
                delay(1000)
                elapsedSeconds++
            }
        }
    }

    val h = elapsedSeconds / 3600
    val m = (elapsedSeconds % 3600) / 60
    val s = elapsedSeconds % 60

    val timeString = if (h > 0) {
        "${h}h ${m.toString().padStart(2, '0')}m ${s.toString().padStart(2, '0')}s"
    } else {
        "${m}m ${s.toString().padStart(2, '0')}s"
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (task.isCompleted) 
                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f) 
            else 
                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = task.name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = if (task.isCompleted) Icons.Default.CheckCircle else Icons.Default.PlayArrow,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = if (task.isCompleted) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.tertiary
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (task.isCompleted) "Completed" else "Ongoing",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
    
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            if (task.isCompleted) MaterialTheme.colorScheme.surfaceVariant
                            else MaterialTheme.colorScheme.primary
                        )
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = timeString,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (task.isCompleted) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onPrimary
                    )
                }

                if (!task.isCompleted) {
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(
                        onClick = { onStop(elapsedSeconds) },
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.errorContainer, RoundedCornerShape(12.dp))
                            .size(40.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Stop,
                            contentDescription = "Stop Task",
                            tint = MaterialTheme.colorScheme.onErrorContainer
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AISuggestionCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f)
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.AutoAwesome,
                contentDescription = "AI Suggestion",
                tint = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = "AI Suggestion for Improvement",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "You've been focused on 'Project API' for an hour. Consider taking a quick 5-minute break to recharge before finishing.",
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                    color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.8f)
                )
            }
        }
    }
}

@Composable
fun AIPromptDialog(onDismiss: () -> Unit, onSubmit: (String) -> Unit) {
    var promptText by remember { mutableStateOf("") }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Ask AI for advice") },
        text = {
            OutlinedTextField(
                value = promptText,
                onValueChange = { promptText = it },
                placeholder = { Text("How can I improve my productivity today?") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )
        },
        confirmButton = {
            Button(onClick = { onSubmit(promptText) }) { Text("Submit") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

@Preview
@Composable
fun HomeScreenPreview() {
    TickrTheme {
        HomeScreen()
    }
}
