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

import org.example.trikr.ui.model.ActiveTask
import org.example.trikr.ui.components.AISuggestionCard
import org.example.trikr.ui.components.AIPromptDialog
import org.example.trikr.ui.components.TaskStatusCard

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

    Box(modifier = Modifier.fillMaxSize()) {
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
                    .weight(1f)
                    .fillMaxWidth()
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
