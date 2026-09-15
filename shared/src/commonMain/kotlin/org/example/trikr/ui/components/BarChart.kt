package org.example.trikr.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun BarChart(
    data: List<Pair<String, Float>>,
    modifier: Modifier = Modifier,
    barColor: Color = MaterialTheme.colorScheme.primary,
    labelColor: Color = MaterialTheme.colorScheme.onSurface
) {
    val textMeasurer = rememberTextMeasurer()
    val maxData = data.maxOfOrNull { it.second } ?: 1f

    Canvas(modifier = modifier) {
        val canvasWidth = size.width
        val canvasHeight = size.height
        
        val barWidth = canvasWidth / (data.size * 2f)
        val spacing = barWidth

        data.forEachIndexed { index, pair ->
            val (label, value) = pair
            val barHeight = (value / maxData) * (canvasHeight - 40.dp.toPx())
            
            val startX = index * (barWidth + spacing) + spacing / 2

            // Draw Bar
            drawRoundRect(
                color = barColor,
                topLeft = Offset(startX, canvasHeight - barHeight - 20.dp.toPx()),
                size = Size(barWidth, barHeight),
                cornerRadius = CornerRadius(8.dp.toPx(), 8.dp.toPx())
            )

            // Draw Label
            val textLayoutResult = textMeasurer.measure(
                text = label,
                style = TextStyle(color = labelColor, fontSize = 12.sp)
            )
            val textX = startX + (barWidth - textLayoutResult.size.width) / 2
            val textY = canvasHeight - 16.dp.toPx()
            
            drawText(
                textLayoutResult = textLayoutResult,
                topLeft = Offset(textX, textY)
            )
        }
    }
}
