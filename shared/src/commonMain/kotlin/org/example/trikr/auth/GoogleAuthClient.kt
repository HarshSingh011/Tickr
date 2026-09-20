package org.example.trikr.auth

import androidx.compose.runtime.Composable

interface GoogleAuthClient {
    suspend fun signIn(): String?
}

@Composable
expect fun rememberGoogleAuthClient(): GoogleAuthClient
