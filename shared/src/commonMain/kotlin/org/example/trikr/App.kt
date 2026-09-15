package org.example.trikr

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import org.example.trikr.theme.TickrTheme
import org.example.trikr.ui.UserFormScreen

@Composable
@Preview
fun App() {
    TickrTheme {
        UserFormScreen(
            onComplete = {
                println("User Form Completed")
            }
        )
    }
}