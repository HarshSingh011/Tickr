package org.example.trikr.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import org.example.trikr.ui.model.ActiveTask

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
