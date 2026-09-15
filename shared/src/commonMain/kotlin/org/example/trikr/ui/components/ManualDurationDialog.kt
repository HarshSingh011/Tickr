package org.example.trikr.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

@Composable
fun ManualDurationDialog(
    initialMinutes: Int,
    onDismiss: () -> Unit,
    onSave: (Int) -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        ManualDurationDialogContent(
            initialMinutes = initialMinutes,
            onDismiss = onDismiss,
            onSave = onSave
        )
    }
}

@Composable
fun ManualDurationDialogContent(
    initialMinutes: Int,
    onDismiss: () -> Unit,
    onSave: (Int) -> Unit
) {
    var hoursText by remember { mutableStateOf((initialMinutes / 60).toString()) }
    var minsText by remember { mutableStateOf((initialMinutes % 60).toString()) }

    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.padding(24.dp)
        ) {
            Text(
                text = "Set Duration",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                OutlinedTextField(
                    value = hoursText,
                    onValueChange = { hoursText = it },
                    label = { Text("Hours") },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                OutlinedTextField(
                    value = minsText,
                    onValueChange = { minsText = it },
                    label = { Text("Mins") },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = onDismiss) {
                    Text("Cancel")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = {
                    val h = hoursText.toIntOrNull() ?: 0
                    val m = minsText.toIntOrNull() ?: 0
                    onSave(h * 60 + m)
                }) {
                    Text("Save")
                }
            }
        }
    }
}

@androidx.compose.ui.tooling.preview.Preview
@Composable
fun ManualDurationDialogPreview() {
    org.example.trikr.theme.TickrTheme {
        ManualDurationDialogContent(
            initialMinutes = 90,
            onDismiss = {},
            onSave = {}
        )
    }
}
