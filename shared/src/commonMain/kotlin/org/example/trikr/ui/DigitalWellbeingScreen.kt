package org.example.trikr.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
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
import org.example.trikr.ui.components.AIPromptDialog
import org.example.trikr.ui.components.AISuggestionCard
import org.example.trikr.ui.components.AppUsageCard
import org.example.trikr.ui.components.BarChart
import org.example.trikr.domain.repositories.TaskRepository
import org.koin.compose.koinInject
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.minus

@Composable
fun DigitalWellbeingScreen() {
    var showAIPromptDialog by remember { mutableStateOf(false) }

    val taskRepository: TaskRepository = koinInject()
    var barChartData by remember { mutableStateOf(listOf<Pair<String, Float>>()) }
    var topTasks by remember { mutableStateOf(listOf<Pair<String, Int>>()) }

    LaunchedEffect(Unit) {
        val result = taskRepository.getLast30DaysStats()
        result.onSuccess { stats ->
            val tz = TimeZone.currentSystemDefault()
            val now = Clock.System.now()
            
            val last7Days = (6 downTo 0).map { i ->
                now.minus(i, DateTimeUnit.DAY, tz).toLocalDateTime(tz).date
            }
            
            val statsMap = stats.groupBy { it.date }
            
            val weeklyData = last7Days.map { date ->
                val dateStr = date.toString() // YYYY-MM-DD
                val dayStats = statsMap[dateStr] ?: emptyList()
                val totalSeconds = dayStats.sumOf { it.total_duration_seconds }
                val totalHours = totalSeconds / 3600f
                val dayName = date.dayOfWeek.name.take(3).lowercase().replaceFirstChar { it.uppercase() }
                dayName to totalHours
            }
            
            if (weeklyData.all { it.second == 0f }) {
                 // if all 0, provide dummy axes but keep 0 values
                 barChartData = last7Days.map { date -> 
                    val dayName = date.dayOfWeek.name.take(3).lowercase().replaceFirstChar { it.uppercase() }
                    dayName to 0f
                 }
            } else {
                barChartData = weeklyData
            }
            
            val aggregatedTasks = mutableMapOf<String, Int>()
            last7Days.forEach { date ->
                val dateStr = date.toString()
                statsMap[dateStr]?.forEach { stat ->
                    aggregatedTasks[stat.name] = (aggregatedTasks[stat.name] ?: 0) + stat.total_duration_seconds
                }
            }
            topTasks = aggregatedTasks.entries
                .sortedByDescending { it.value }
                .take(3)
                .map { it.key to (it.value / 60) } 
        }
    }

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
                .padding(top = 48.dp, start = 24.dp, end = 24.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = "Digital Wellbeing",
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
                text = "Your screen time and focus statistics",
                fontSize = 16.sp,
                color = Color.LightGray,
            )
            Spacer(modifier = Modifier.height(32.dp))

            // Chart Section
            Text(
                text = "Weekly Focus Time",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier.fillMaxWidth().height(250.dp),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.9f)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                if (barChartData.isNotEmpty()) {
                    BarChart(
                        data = barChartData,
                        modifier = Modifier.fillMaxSize().padding(20.dp),
                        barColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // App Usage Section
            Text(
                text = "Top Activities",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Spacer(modifier = Modifier.height(16.dp))
            
            val colors = listOf(Color(0xFFE1306C), Color(0xFFFF0000), Color(0xFF4285F4))
            
            if (topTasks.isEmpty()) {
                Text("No activities recorded this week.", color = Color.LightGray, fontSize = 14.sp)
            } else {
                topTasks.forEachIndexed { index, pair ->
                    AppUsageCard(appName = pair.first, durationMinutes = pair.second, iconColor = colors[index % colors.size])
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // AI Suggestion Section
            Text(
                text = "Insights",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 12.dp)
            )
            AISuggestionCard()
            
            Spacer(modifier = Modifier.height(150.dp)) // padding for floating nav
        }

        // Floating Action Button
        FloatingActionButton(
            onClick = { showAIPromptDialog = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 110.dp, end = 24.dp), // Lifted above the bottom navigation bar
            containerColor = MaterialTheme.colorScheme.tertiary,
            contentColor = MaterialTheme.colorScheme.onTertiary,
            elevation = FloatingActionButtonDefaults.elevation(8.dp)
        ) {
            Icon(Icons.Default.AutoAwesome, contentDescription = "AI Prompt")
        }
    }
}

@androidx.compose.ui.tooling.preview.Preview
@Composable
fun DigitalWellbeingScreenPreview() {
    org.example.trikr.theme.TickrTheme {
        DigitalWellbeingScreen()
    }
}
