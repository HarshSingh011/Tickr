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

@Composable
fun DigitalWellbeingScreen() {
    var showAIPromptDialog by remember { mutableStateOf(false) }

    // Unified premium gradient background (Blue fading to bottom)
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
            
            val dummyData = listOf(
                "Mon" to 3.5f,
                "Tue" to 4.2f,
                "Wed" to 2.8f,
                "Thu" to 5.1f,
                "Fri" to 3.9f,
                "Sat" to 1.5f,
                "Sun" to 2.0f
            )

            Card(
                modifier = Modifier.fillMaxWidth().height(250.dp),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.9f)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                BarChart(
                    data = dummyData,
                    modifier = Modifier.fillMaxSize().padding(20.dp),
                    barColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // App Usage Section
            Text(
                text = "App Usage",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Spacer(modifier = Modifier.height(16.dp))
            
            AppUsageCard(appName = "Instagram", durationMinutes = 85, iconColor = Color(0xFFE1306C))
            AppUsageCard(appName = "YouTube", durationMinutes = 120, iconColor = Color(0xFFFF0000))
            AppUsageCard(appName = "Chrome", durationMinutes = 45, iconColor = Color(0xFF4285F4))

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
