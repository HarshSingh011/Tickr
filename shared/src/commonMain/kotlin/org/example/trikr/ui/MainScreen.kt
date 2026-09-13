package org.example.trikr.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.example.trikr.alarm.presentation.AlarmScreen
import org.example.trikr.theme.TickrTheme

@Composable
fun MainScreen() {
    var selectedTab by remember { mutableStateOf(0) }

    Box(modifier = Modifier.fillMaxSize()) {
        // 1. Content Area (Scrolls underneath the floating nav bar)
        when (selectedTab) {
            0 -> HomeScreen()
            1 -> AlarmScreen()
            2 -> DigitalWellbeingScreen()
            3 -> ProfileScreen()
        }

        // 2. Liquid Glass Floating Bottom Navigation Bar
        NavigationBar(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(horizontal = 24.dp, vertical = 24.dp)
                .clip(RoundedCornerShape(32.dp))
                .border(
                    width = 1.5.dp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(32.dp)
                ),
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f),
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
        ) {
            NavigationBarItem(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                label = { Text("Home") },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.8f)
                )
            )
            NavigationBarItem(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                icon = { Icon(Icons.Default.Alarm, contentDescription = "Alarm") },
                label = { Text("Alarm") },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.8f)
                )
            )
            NavigationBarItem(
                selected = selectedTab == 2,
                onClick = { selectedTab = 2 },
                icon = { Icon(Icons.Default.Spa, contentDescription = "Wellbeing") },
                label = { Text("Wellbeing") },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.8f)
                )
            )
            NavigationBarItem(
                selected = selectedTab == 3,
                onClick = { selectedTab = 3 },
                icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
                label = { Text("Profile") },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.8f)
                )
            )
        }
    }
}

@Preview
@Composable
fun MainScreenPreview() {
    TickrTheme {
        MainScreen()
    }
}
