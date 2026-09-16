package org.example.trikr.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import org.example.trikr.theme.TickrTheme
import org.example.trikr.ui.components.AnimatedTrikrLogo

@Composable
fun SplashScreen(onSplashFinished: () -> Unit) {
    AnimatedTrikrLogo(
        onAnimationFinished = onSplashFinished
    )
}

@Preview
@Composable
fun SplashScreenPreview() {
    TickrTheme {
        SplashScreen(onSplashFinished = {})
    }
}
