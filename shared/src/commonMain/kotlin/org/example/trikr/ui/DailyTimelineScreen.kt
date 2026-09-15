package org.example.trikr.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import kotlin.math.abs

data class TimelineTask(
    val id: String,
    val name: String,
    val startTime: String,
    val endTime: String,
    val color: Color
)

fun getDurationMins(start: String, end: String): Int {
    return try {
        val startHour = start.substring(11, 13).toInt()
        val startMin = start.substring(14, 16).toInt()
        val endHour = end.substring(11, 13).toInt()
        val endMin = end.substring(14, 16).toInt()

        val startTotalMins = startHour * 60 + startMin
        val endTotalMins = endHour * 60 + endMin
        abs(endTotalMins - startTotalMins)
    } catch (e: Exception) {
        0
    }
}

fun calculateDurationText(start: String, end: String): String {
    val duration = getDurationMins(start, end)
    val hours = duration / 60
    val mins = duration % 60
    
    return if (hours > 0 && mins > 0) "${hours}h ${mins}m"
    else if (hours > 0) "${hours}h"
    else "${mins}m"
}

fun formatTime(isoString: String): String {
    return try {
        isoString.substring(11, 16)
    } catch(e: Exception) { "" }
}

@Composable
fun TaskDetailsCard(task: TimelineTask) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.9f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                val iconGradient = Brush.linearGradient(
                    colors = listOf(task.color.copy(alpha = 0.6f), task.color)
                )

                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(iconGradient),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = task.name.take(1).uppercase(),
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 20.sp,
                        color = Color.White
                    )
                }
                
                Spacer(modifier = Modifier.width(16.dp))
                
                Column {
                    Text(
                        text = task.name,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "${formatTime(task.startTime)} - ${formatTime(task.endTime)}",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            }
            
            Text(
                text = calculateDurationText(task.startTime, task.endTime),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun TimelineSingleBar(tasks: List<TimelineTask>) {
    val otherColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
    
    Canvas(modifier = Modifier.fillMaxSize()) {
        val totalHeight = size.height
        val barWidth = size.width
        val xOffset = 0f

        var currentBottomFraction = 0f

        val sortedTasks = tasks.sortedByDescending { getDurationMins(it.startTime, it.endTime) }
        val topTasks = sortedTasks.take(4)
        val otherTasks = sortedTasks.drop(4)

        topTasks.forEach { task ->
            val durationMins = getDurationMins(task.startTime, task.endTime)
            if (durationMins > 0) {
                val durationFraction = durationMins / (24f * 60f)

                val taskBottom = (1f - currentBottomFraction) * totalHeight
                val taskHeight = durationFraction * totalHeight
                val taskTop = taskBottom - taskHeight

                drawRoundRect(
                    color = task.color,
                    topLeft = Offset(xOffset, taskTop),
                    size = Size(barWidth, taskHeight),
                    cornerRadius = CornerRadius(4f, 4f) 
                )

                currentBottomFraction += durationFraction
            }
        }

        if (otherTasks.isNotEmpty()) {
            val otherDurationMins = otherTasks.sumOf { getDurationMins(it.startTime, it.endTime) }
            val durationFraction = otherDurationMins / (24f * 60f)

            val taskBottom = (1f - currentBottomFraction) * totalHeight
            val taskHeight = durationFraction * totalHeight
            val taskTop = taskBottom - taskHeight

            drawRoundRect(
                color = otherColor,
                topLeft = Offset(xOffset, taskTop),
                size = Size(barWidth, taskHeight),
                cornerRadius = CornerRadius(4f, 4f) 
            )
            
            currentBottomFraction += durationFraction
        }
    }
}

@Composable
fun MonthlyTimelineGraph(
    data: Map<Int, List<TimelineTask>>,
    selectedDay: Int,
    onDaySelected: (Int) -> Unit
) {
    val scrollState = rememberScrollState()
    
    LaunchedEffect(scrollState.maxValue) {
        if (scrollState.maxValue > 0) {
            scrollState.scrollTo(scrollState.maxValue)
        }
    }
    
    Row(modifier = Modifier.fillMaxHeight().padding(horizontal = 8.dp)) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(vertical = 8.dp),
            horizontalAlignment = Alignment.End
        ) {
            Column(
                modifier = Modifier.weight(1f), 
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.End
            ) {
                Text("24h", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
                Text("18h", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
                Text("12h", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
                Text("6h",  fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
                Text("0h",  fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(" ", fontSize = 12.sp) 
        }

        Spacer(modifier = Modifier.width(12.dp))

        BoxWithConstraints(modifier = Modifier.weight(1f)) {
            val spacing = 8.dp
            val visibleColumns = 7
            val columnWidth = (maxWidth - (spacing * (visibleColumns - 1))) / visibleColumns

            Row(
                modifier = Modifier
                    .fillMaxHeight()
                    .horizontalScroll(scrollState),
                horizontalArrangement = Arrangement.spacedBy(spacing)
            ) {
                for (day in 1..30) {
                    val isSelected = selectedDay == day
                    Column(
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(columnWidth)
                            .clip(RoundedCornerShape(8.dp))
                            .clickable { onDaySelected(day) }
                            .then(
                                if (isSelected) Modifier.background(MaterialTheme.colorScheme.primary.copy(alpha = 0.15f))
                                else Modifier
                            )
                            .padding(vertical = 8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(modifier = Modifier.weight(1f).fillMaxWidth(0.5f), contentAlignment = Alignment.Center) {
                            TimelineSingleBar(tasks = data[day] ?: emptyList())
                        }
                        
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = day.toString(),
                            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DailyTimelineScreen() {
    var selectedDay by remember { mutableStateOf(30) }

    val colorPrimary = MaterialTheme.colorScheme.primary
    val colorTertiary = MaterialTheme.colorScheme.tertiary
    val colorSecondary = MaterialTheme.colorScheme.secondary

    val baseTasks = listOf(
        TimelineTask("1", "Gym", "2026-09-15T07:00:00Z", "2026-09-15T08:00:00Z", colorPrimary),
        TimelineTask("2", "Work", "2026-09-15T09:00:00Z", "2026-09-15T17:00:00Z", colorTertiary),
        TimelineTask("3", "Personal Growth", "2026-09-15T18:00:00Z", "2026-09-15T20:00:00Z", colorSecondary)
    )

    val chaoticDayTasks = baseTasks + (4..15).map { i ->
        val hour = 20 + ((i - 4) / 4)
        val min = ((i - 4) % 4) * 15
        val startStr = "2026-09-15T${hour.toString().padStart(2, '0')}:${min.toString().padStart(2, '0')}:00Z"
        val endStr = "2026-09-15T${hour.toString().padStart(2, '0')}:${(min + 10).toString().padStart(2, '0')}:00Z"
        
        TimelineTask(
            id = i.toString(),
            name = "Mini Task $i",
            startTime = startStr,
            endTime = endStr,
            color = Color(0xFF9E9E9E)
        )
    }

    val monthlyData = remember {
        (1..30).associateWith { day ->
            when {
                day == 30 -> chaoticDayTasks
                day % 7 == 0 -> listOf(baseTasks[0], baseTasks[2])
                day % 5 == 0 -> listOf(baseTasks[1], baseTasks[2])
                day % 3 == 0 -> baseTasks.drop(1)
                else -> baseTasks
            }
        }
    }

    val backgroundBrush = Brush.verticalGradient(
        colors = listOf(
            MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
            MaterialTheme.colorScheme.background,
            MaterialTheme.colorScheme.background
        )
    )

    Box(modifier = Modifier.fillMaxSize().background(backgroundBrush)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 48.dp, start = 24.dp, end = 24.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = "Monthly Overview",
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
                text = "Your daily task distribution",
                fontSize = 16.sp,
                color = Color.LightGray,
            )
            Spacer(modifier = Modifier.height(32.dp))

            Card(
                modifier = Modifier.fillMaxWidth().height(350.dp),
                shape = RoundedCornerShape(32.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column(modifier = Modifier.fillMaxSize().padding(vertical = 24.dp)) {
                    Text(
                        text = "September",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(horizontal = 24.dp)
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    Box(modifier = Modifier.weight(1f)) {
                        MonthlyTimelineGraph(
                            data = monthlyData,
                            selectedDay = selectedDay,
                            onDaySelected = { selectedDay = it }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Day $selectedDay Details",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(16.dp))

            val tasksForDay = monthlyData[selectedDay] ?: emptyList()

            if (tasksForDay.isEmpty()) {
                Text(
                    text = "No tasks recorded for this day.",
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    modifier = Modifier.padding(vertical = 24.dp)
                )
            } else {
                tasksForDay.forEach { task ->
                    TaskDetailsCard(task)
                }
            }
            
            Spacer(modifier = Modifier.height(150.dp))
        }

    }
}

@androidx.compose.ui.tooling.preview.Preview
@Composable
fun DailyTimelineScreenPreview() {
    org.example.trikr.theme.TickrTheme {
        DailyTimelineScreen()
    }
}
