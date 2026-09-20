package org.example.trikr.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.example.trikr.domain.repositories.TaskRepository
import org.koin.compose.koinInject
import org.example.trikr.theme.TickrTheme
import org.example.trikr.ui.model.ActiveTask
import org.example.trikr.ui.components.AISuggestionCard
import org.example.trikr.ui.components.AIPromptDialog
import org.example.trikr.ui.components.TaskStatusCard
import kotlin.time.Clock
import kotlin.time.Instant

@Composable
fun HomeScreen(
    onNavigateToTimeline: () -> Unit = {}
) {
    var showAIPromptDialog by remember { mutableStateOf(false) }
    var showTaskNameDialog by remember { mutableStateOf(false) }
    var taskNameInput by remember { mutableStateOf("") }
    
    val taskRepository: TaskRepository = koinInject()
    val scope = rememberCoroutineScope()
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val tasks = remember { mutableStateListOf<ActiveTask>() }
    
    LaunchedEffect(Unit) {
        val result = taskRepository.getTodaysTasks()
        result.onSuccess { dtoList ->
            val now = Clock.System.now()
            tasks.clear()
            tasks.addAll(dtoList.map { dto ->
                val isCompleted = dto.status == "completed"
                val elapsed = if (isCompleted) {
                    dto.duration_seconds
                } else {
                    try {
                        val startTime = Instant.parse(dto.start_time)
                        (now - startTime).inWholeSeconds.toInt()
                    } catch (e: Exception) {
                        0
                    }
                }
                ActiveTask(
                    id = dto.id,
                    name = dto.name,
                    isCompleted = isCompleted,
                    timeElapsedSeconds = elapsed
                )
            })
        }
    }
    val sortedTasks = tasks.sortedBy { it.isCompleted }
    val ongoingTask = tasks.find { !it.isCompleted }
    val hasOngoingTask = ongoingTask != null

    var activeTimerSeconds by remember(ongoingTask) { 
        mutableStateOf(ongoingTask?.timeElapsedSeconds ?: 0) 
    }
    
    LaunchedEffect(ongoingTask) {
        if (ongoingTask != null) {
            while (true) {
                delay(1000)
                activeTimerSeconds++
            }
        }
    }

    val h = activeTimerSeconds / 3600
    val m = (activeTimerSeconds % 3600) / 60
    val s = activeTimerSeconds % 60
    
    val mainTimerText = "${h.toString().padStart(2, '0')}:${m.toString().padStart(2, '0')}:${s.toString().padStart(2, '0')}"

    val backgroundBrush = Brush.verticalGradient(
        colors = listOf(
            MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
            MaterialTheme.colorScheme.background,
            MaterialTheme.colorScheme.background
        )
    )

    Box(modifier = Modifier.fillMaxSize().background(backgroundBrush)) {
        if (showAIPromptDialog) {
            AIPromptDialog(
                onDismiss = { showAIPromptDialog = false },
                onSubmit = { _ -> showAIPromptDialog = false }
            )
        }

        if (showTaskNameDialog) {
            AlertDialog(
                onDismissRequest = { 
                    showTaskNameDialog = false 
                    errorMessage = null
                },
                title = { Text("Start New Task") },
                text = {
                    Column {
                        OutlinedTextField(
                            value = taskNameInput,
                            onValueChange = { taskNameInput = it },
                            label = { Text("Task Name") },
                            singleLine = true,
                            isError = errorMessage != null
                        )
                        if (errorMessage != null) {
                            Text(
                                text = errorMessage!!,
                                color = MaterialTheme.colorScheme.error,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            if (taskNameInput.isBlank()) {
                                errorMessage = "Task name cannot be empty"
                                return@Button
                            }
                            if (isLoading) return@Button
                            
                            scope.launch {
                                isLoading = true
                                errorMessage = null
                                val result = taskRepository.startTask(taskNameInput)
                                result.onSuccess {
                                    // Refetch or simply add optimistic UI update. Since we don't have the new ID from the backend easily yet,
                                    // we can just re-fetch the tasks to get the true state and ID.
                                    val refreshResult = taskRepository.getTodaysTasks()
                                    refreshResult.onSuccess { dtoList ->
                                        val now = Clock.System.now()
                                        tasks.clear()
                                        tasks.addAll(dtoList.map { dto ->
                                            val isCompleted = dto.status == "completed"
                                            val elapsed = if (isCompleted) {
                                                dto.duration_seconds
                                            } else {
                                                try {
                                                    val startTime = Instant.parse(dto.start_time)
                                                    (now - startTime).inWholeSeconds.toInt()
                                                } catch (e: Exception) {
                                                    0
                                                }
                                            }
                                            ActiveTask(
                                                id = dto.id,
                                                name = dto.name,
                                                isCompleted = isCompleted,
                                                timeElapsedSeconds = elapsed
                                            )
                                        })
                                    }
                                    showTaskNameDialog = false
                                    taskNameInput = ""
                                }.onFailure { e ->
                                    errorMessage = "Failed to start task: \${e.message}"
                                }
                                isLoading = false
                            }
                        }
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                        } else {
                            Text("Start")
                        }
                    }
                },
                dismissButton = {
                    TextButton(onClick = { 
                        showTaskNameDialog = false 
                        errorMessage = null
                    }) {
                        Text("Cancel")
                    }
                }
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 64.dp, bottom = 32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = if (hasOngoingTask) "Ongoing Task Time" else "No Active Task",
                        fontSize = 14.sp,
                        color = Color.LightGray,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = mainTimerText,
                        style = TextStyle(
                            brush = Brush.linearGradient(
                                colors = listOf(Color.White, Color.LightGray)
                            )
                        ),
                        fontSize = 48.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 2.sp
                    )
                }
            }

            AISuggestionCard()

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Today's Tasks",
                    style = TextStyle(
                        brush = Brush.linearGradient(
                            colors = listOf(Color.Black, Color.DarkGray)
                        )
                    ),
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
                
                TextButton(
                    onClick = onNavigateToTimeline,
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text(
                        text = "View All",
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            if (!hasOngoingTask) {
                Button(
                    onClick = {
                        taskNameInput = ""
                        errorMessage = null
                        showTaskNameDialog = true
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
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                items(sortedTasks, key = { it.id }) { task ->
                    TaskStatusCard(
                        task = task,
                        onStop = { finalTime ->
                            scope.launch {
                                val result = taskRepository.stopTask(task.id)
                                result.onSuccess {
                                    val index = tasks.indexOfFirst { it.id == task.id }
                                    if (index != -1) {
                                        tasks[index] = task.copy(isCompleted = true, timeElapsedSeconds = finalTime)
                                    }
                                }.onFailure { e ->
                                    errorMessage = "Failed to stop task: ${e.message}"
                                }
                            }
                        }
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(200.dp))
                }
            }
        }

        FloatingActionButton(
            onClick = { showAIPromptDialog = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 110.dp, end = 24.dp),
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ) {
            Icon(Icons.Default.AutoAwesome, contentDescription = "AI Prompt")
        }
    }
}



@Preview
@Composable
fun HomeScreenPreview() {
    TickrTheme {
        HomeScreen()
    }
}
