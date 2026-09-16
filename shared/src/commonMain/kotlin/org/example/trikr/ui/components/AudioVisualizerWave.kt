package org.example.trikr.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import kotlin.math.PI
import kotlin.math.sin

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
