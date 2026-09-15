package org.example.trikr.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.example.trikr.ui.formatDuration

@Composable
fun DurationSlider(
    title: String,
    durationMinutes: Int,
    onValueChange: (Int) -> Unit,
    onDurationClick: () -> Unit,
    maxMinutes: Int = 1440
) {
    Column(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = formatDuration(durationMinutes),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f))
                    .clickable(onClick = onDurationClick)
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            )
        }
        Slider(
            value = durationMinutes.toFloat(),
            onValueChange = { onValueChange(it.toInt()) },
            valueRange = 0f..maxMinutes.toFloat(),
            colors = SliderDefaults.colors(
                thumbColor = Color.DarkGray,
                activeTrackColor = Color.DarkGray,
                inactiveTrackColor = Color.Gray.copy(alpha = 0.5f)
            )
        )
    }
}
