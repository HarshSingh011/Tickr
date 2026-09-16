package org.example.trikr

import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import org.example.trikr.theme.TickrTheme
import org.example.trikr.ui.SplashScreen
import org.example.trikr.ui.LoginScreen
import org.example.trikr.ui.MainScreen
import org.example.trikr.ui.UserFormScreen

enum class Screen {
    SPLASH,
    LOGIN,
    USER_FORM,
    MAIN
}

@Composable
@Preview
fun App() {
    var currentScreen by remember { mutableStateOf(Screen.SPLASH) }

    TickrTheme {
        when (currentScreen) {
            Screen.SPLASH -> {
                SplashScreen(
                    onSplashFinished = { currentScreen = Screen.LOGIN }
                )
            }
            Screen.LOGIN -> {
                LoginScreen(
                    onGoogleLoginClick = { currentScreen = Screen.USER_FORM }
                )
            }
            Screen.USER_FORM -> {
                UserFormScreen(
                    onComplete = { currentScreen = Screen.MAIN }
                )
            }
            Screen.MAIN -> {
                MainScreen()
            }
        }
    }
}