package org.example.trikr.auth

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

class IosGoogleAuthClient : GoogleAuthClient {
    override suspend fun signIn(): String? {
        // iOS implementation would use GoogleSignIn SDK for iOS
        // For now, return null
        println("Google Sign-In is not yet implemented on iOS")
        return null
    }
}

@Composable
actual fun rememberGoogleAuthClient(): GoogleAuthClient {
    return remember { IosGoogleAuthClient() }
}
