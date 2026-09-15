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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
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
    val initialTasks = listOf(
        ActiveTask(1, "Morning Workout", isCompleted = true, timeElapsedSeconds = 5400),
        ActiveTask(2, "Deep Work: Project API", isCompleted = false, timeElapsedSeconds = 3600),
        ActiveTask(3, "Read a book", isCompleted = true, timeElapsedSeconds = 7200)
    )
    val tasks = remember { mutableStateListOf(*initialTasks.toTypedArray()) }
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
                    onClick = { },
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
