package org.example.trikr.ui.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import kotlin.math.PI
import kotlin.math.sin
import org.example.trikr.theme.TickrTheme

@Composable
fun AIPromptDialog(onDismiss: () -> Unit, onSubmit: (String) -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        AIPromptDialogContent(onDismiss = onDismiss, onSubmit = onSubmit)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AIPromptDialogContent(onDismiss: () -> Unit, onSubmit: (String) -> Unit) {
    var promptText by remember { mutableStateOf("") }
    var isRecording by remember { mutableStateOf(false) }
    
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(24.dp, RoundedCornerShape(32.dp)),
            shape = RoundedCornerShape(32.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            border = BorderStroke(
                width = 1.dp,
                brush = Brush.linearGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                        Color.Transparent,
                        MaterialTheme.colorScheme.tertiary.copy(alpha = 0.3f)
                    )
                )
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.05f),
                                Color.Transparent
                            )
                        )
                    )
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(
                                    Brush.linearGradient(
                                        colors = listOf(
                                            MaterialTheme.colorScheme.primary,
                                            MaterialTheme.colorScheme.tertiary
                                        )
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.AutoAwesome,
                                contentDescription = "AI",
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(
                                text = "AI Assistant",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            AnimatedContent<Boolean>(
                                targetState = isRecording,
                                label = "recording_text"
                            ) { recording ->
                                Text(
                                    text = if (recording) "Listening..." else "Always here to help",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }
                    IconButton(
                        onClick = onDismiss, 
                        modifier = Modifier
                            .size(36.dp)
                            .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f), CircleShape)
                    ) {
                        Icon(
                            Icons.Default.Close, 
                            contentDescription = "Close", 
                            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(28.dp))

                AnimatedContent(
                    targetState = isRecording,
                    label = "input_area"
                ) { recording ->
                    if (recording) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(min = 120.dp, max = 160.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                AudioVisualizerWave()
                                Spacer(modifier = Modifier.height(24.dp))
                                Text(
                                    text = "AI is listening to your prompt...",
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    } else {
                        OutlinedTextField(
                            value = promptText,
                            onValueChange = { promptText = it },
                            placeholder = { 
                                Text(
                                    "e.g., How can I improve my focus today?",
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                                    fontSize = 15.sp
                                ) 
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(min = 120.dp, max = 160.dp),
                            shape = RoundedCornerShape(24.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = MaterialTheme.colorScheme.primary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.15f),
                                focusedContainerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.02f),
                                unfocusedContainerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.02f)
                            ),
                            textStyle = TextStyle(fontSize = 16.sp),
                            trailingIcon = {
                                if (promptText.isNotBlank()) {
                                    IconButton(
                                        onClick = { onSubmit(promptText) },
                                        modifier = Modifier
                                            .padding(8.dp)
                                            .background(MaterialTheme.colorScheme.primary, CircleShape)
                                    ) {
                                        Icon(
                                            Icons.Default.Send,
                                            contentDescription = "Send",
                                            tint = Color.White,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Box(
            modifier = Modifier.size(80.dp),
            contentAlignment = Alignment.Center
        ) {
            GlassmorphicMicButton(
                isRecording = isRecording,
                onClick = { isRecording = !isRecording }
            )
        }
    }
}

@Composable
fun GlassmorphicMicButton(isRecording: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(72.dp)
            .shadow(
                elevation = if (isRecording) 24.dp else 12.dp, 
                shape = CircleShape, 
                spotColor = if (isRecording) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
            )
            .clip(CircleShape)
            .background(
                Brush.linearGradient(
                    listOf(
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                        MaterialTheme.colorScheme.tertiary.copy(alpha = 0.05f)
                    )
                )
            )
            .border(
                width = 1.dp,
                brush = Brush.linearGradient(
                    listOf(
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                        Color.Transparent,
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
                    )
                ),
                shape = CircleShape
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            if (isRecording) MaterialTheme.colorScheme.error.copy(alpha = 0.3f) 
                            else MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                            Color.Transparent
                        )
                    ),
                    shape = CircleShape
                )
        )
        
        Icon(
            imageVector = Icons.Default.Mic,
            contentDescription = if (isRecording) "Stop Recording" else "Start Recording",
            tint = if (isRecording) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(32.dp)
        )
    }
}

@Composable
fun AudioVisualizerWave() {
    val infiniteTransition = rememberInfiniteTransition()
    
    val phase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = (2.0 * PI).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(2500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    val amplitudeShift by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    val primaryColor = MaterialTheme.colorScheme.primary
    val tertiaryColor = MaterialTheme.colorScheme.tertiary
    val wavesCount = 6

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
    ) {
        val w = size.width
        val h = size.height
        val centerY = h / 2f

        for (index in 0 until wavesCount) {
            val path = Path()
            
            val freqMultiplier = 0.5f + ((index * 13) % 17) * 0.1f
            val ampScale = 0.4f + ((index * 7) % 5) * 0.15f
            val staticPhaseOffset = ((index * 23) % 11) * 0.5f
            val phaseDirection = if (index % 2 == 0) 1f else -1f
            
            val phaseOffset = (phase * phaseDirection) + staticPhaseOffset
            val currentAmplitude = (h / 2f) * amplitudeShift * ampScale
            
            val baseColor = if (index % 2 == 0) primaryColor else tertiaryColor
            val alpha = 0.9f - ((index * 3) % 4) * 0.15f
            
            val steps = 100
            for (i in 0..steps) {
                val normalizedX = i.toFloat() / steps
                val x = normalizedX * w
                
                val y = centerY + sin((normalizedX * PI * 2.0 * freqMultiplier) + phaseOffset).toFloat() * currentAmplitude
                
                if (i == 0) {
                    path.moveTo(x, y)
                } else {
                    path.lineTo(x, y)
                }
            }

            drawPath(
                path = path,
                color = baseColor.copy(alpha = alpha.coerceAtLeast(0.1f)),
                style = Stroke(
                    width = (4.5f - (index * 0.4f)).dp.toPx(),
                    cap = StrokeCap.Round,
                    join = StrokeJoin.Round
                )
            )
        }
    }
}

@Preview
@Composable
fun AIPromptDialogPreview() {
    TickrTheme {
        AIPromptDialogContent(onDismiss = {}, onSubmit = {})
    }
}
