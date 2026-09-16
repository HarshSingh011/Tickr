package org.example.trikr.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun AnimatedTrikrLogo(onAnimationFinished: () -> Unit) {
    val transitionState = remember {
        MutableTransitionState(false).apply {
            targetState = true
        }
    }
    
    val transition = updateTransition(transitionState, label = "")

    val tProgress by transition.animateFloat(
        transitionSpec = { tween(durationMillis = 1200, easing = FastOutSlowInEasing) },
        label = ""
    ) { state -> if (state) 1f else 0f }

    val orbitProgress by transition.animateFloat(
        transitionSpec = { tween(durationMillis = 1500, delayMillis = 600, easing = CubicBezierEasing(0.2f, 0.0f, 0.0f, 1.0f)) },
        label = ""
    ) { state -> if (state) 1f else 0f }

    val clockProgress by transition.animateFloat(
        transitionSpec = { tween(durationMillis = 1000, delayMillis = 1200, easing = FastOutSlowInEasing) },
        label = ""
    ) { state -> if (state) 1f else 0f }

    val fillAlpha by transition.animateFloat(
        transitionSpec = { tween(durationMillis = 1200, delayMillis = 2000, easing = EaseOutExpo) },
        label = ""
    ) { state -> if (state) 1f else 0f }

    val textOffsetY by transition.animateFloat(
        transitionSpec = { tween(durationMillis = 1000, delayMillis = 2300, easing = FastOutSlowInEasing) },
        label = ""
    ) { state -> if (state) 0f else 60f }

    val textAlpha by transition.animateFloat(
        transitionSpec = { tween(durationMillis = 800, delayMillis = 2300, easing = LinearEasing) },
        label = ""
    ) { state -> if (state) 1f else 0f }

    LaunchedEffect(transitionState.currentState) {
        if (transitionState.currentState) {
            delay(3800L)
            onAnimationFinished()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primaryContainer,
                        MaterialTheme.colorScheme.background
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(
                modifier = Modifier
                    .size(180.dp)
                    .padding(16.dp)
                    .drawWithCache {
                        val w = size.width
                        val h = size.height

                        val tTopPath = Path().apply {
                            moveTo(w * 0.20f, h * 0.38f)
                            lineTo(w * 0.60f, h * 0.35f)
                        }
                        val tStemPath = Path().apply {
                            moveTo(w * 0.47f, h * 0.36f)
                            lineTo(w * 0.35f, h * 0.72f)
                        }
                        val clockCircle = Path().apply {
                            addOval(Rect(w * 0.55f, h * 0.45f, w * 0.81f, h * 0.71f))
                        }
                        val clockHour = Path().apply {
                            moveTo(w * 0.68f, h * 0.58f)
                            lineTo(w * 0.68f, h * 0.50f)
                        }
                        val clockMinute = Path().apply {
                            moveTo(w * 0.68f, h * 0.58f)
                            lineTo(w * 0.75f, h * 0.63f)
                        }
                        val orbitPath = Path().apply {
                            addArc(
                                oval = Rect(w * 0.02f, h * 0.02f, w * 0.98f, h * 0.98f),
                                startAngleDegrees = -90f,
                                sweepAngleDegrees = 240f
                            )
                        }

                        val pathMeasure = PathMeasure()
                        
                        fun getPathData(path: Path): Pair<Float, FloatArray> {
                            pathMeasure.setPath(path, forceClosed = false)
                            val length = pathMeasure.length
                            return length to floatArrayOf(length, length)
                        }

                        val (orbitLen, orbitIntervals) = getPathData(orbitPath)
                        val (tTopLen, tTopIntervals) = getPathData(tTopPath)
                        val (tStemLen, tStemIntervals) = getPathData(tStemPath)
                        val (clockCircleLen, clockCircleIntervals) = getPathData(clockCircle)
                        val (clockHourLen, clockHourIntervals) = getPathData(clockHour)
                        val (clockMinuteLen, clockMinuteIntervals) = getPathData(clockMinute)

                        val traceBrush = Brush.linearGradient(
                            colors = listOf(Color(0xFF33D0FF), Color(0xFFA055FF)),
                            start = Offset(0f, 0f),
                            end = Offset(w, h)
                        )
                        val orbitBrush = Brush.linearGradient(
                            colors = listOf(Color(0xFF2299FF), Color.Transparent),
                            start = Offset(w, 0f),
                            end = Offset(0f, h)
                        )
                        val fillBrush = Brush.linearGradient(
                            colors = listOf(Color(0xFF2CB5FF), Color(0xFF6B4DFF)),
                            start = Offset(0f, 0f),
                            end = Offset(w, h)
                        )
                        
                        val filledStrokeStyle18 = Stroke(w * 0.18f, cap = StrokeCap.Round, join = StrokeJoin.Round)
                        val filledStrokeStyle8 = Stroke(w * 0.08f, cap = StrokeCap.Round, join = StrokeJoin.Round)
                        val filledStrokeStyle4 = Stroke(w * 0.04f, cap = StrokeCap.Round, join = StrokeJoin.Round)

                        onDrawBehind {
                            fun drawTrace(
                                path: Path, brush: Brush, strokeWidth: Float, 
                                progress: Float, length: Float, intervals: FloatArray
                            ) {
                                if (progress == 0f) return
                                val dashEffect = PathEffect.dashPathEffect(
                                    intervals = intervals,
                                    phase = length - (length * progress)
                                )
                                drawPath(
                                    path = path,
                                    brush = brush,
                                    style = Stroke(
                                        width = strokeWidth,
                                        cap = StrokeCap.Round,
                                        join = StrokeJoin.Round,
                                        pathEffect = dashEffect
                                    )
                                )
                            }

                            drawTrace(orbitPath, orbitBrush, w * 0.04f, orbitProgress, orbitLen, orbitIntervals)
                            drawTrace(tTopPath, traceBrush, w * 0.18f, tProgress, tTopLen, tTopIntervals)
                            drawTrace(tStemPath, traceBrush, w * 0.18f, tProgress, tStemLen, tStemIntervals)
                            drawTrace(clockCircle, traceBrush, w * 0.08f, clockProgress, clockCircleLen, clockCircleIntervals)
                            drawTrace(clockHour, traceBrush, w * 0.08f, clockProgress, clockHourLen, clockHourIntervals)
                            drawTrace(clockMinute, traceBrush, w * 0.08f, clockProgress, clockMinuteLen, clockMinuteIntervals)

                            if (fillAlpha > 0f) {
                                drawPath(tTopPath, fillBrush, fillAlpha, filledStrokeStyle18)
                                drawPath(tStemPath, fillBrush, fillAlpha, filledStrokeStyle18)
                                drawPath(clockCircle, fillBrush, fillAlpha, filledStrokeStyle8)
                                drawPath(clockHour, fillBrush, fillAlpha, filledStrokeStyle8)
                                drawPath(clockMinute, fillBrush, fillAlpha, filledStrokeStyle8)
                                drawPath(orbitPath, fillBrush, fillAlpha, filledStrokeStyle4)
                            }
                        }
                    }
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .graphicsLayer {
                        translationY = textOffsetY
                        alpha = textAlpha
                    }
            ) {
                Text(
                    text = "Trikr",
                    fontSize = 46.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.primary,
                    letterSpacing = 2.sp
                )
                Text(
                    text = "Master your time",
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.secondary,
                    fontWeight = FontWeight.Medium,
                    letterSpacing = 1.sp
                )
            }
        }
    }
}
