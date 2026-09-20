package org.example.trikr.auth

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.GetCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class AndroidGoogleAuthClient(private val context: Context) : GoogleAuthClient {
    override suspend fun signIn(): String? {
        val credentialManager = CredentialManager.create(context)
        
        // Web Client ID from Google Cloud Console
        val webClientId = "246198754561-464jbqbb34v96o3l591vbrs0fn63mh2s.apps.googleusercontent.com" 
        
        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(webClientId)
            .setAutoSelectEnabled(true)
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        return try {
            val result = credentialManager.getCredential(
                request = request,
                context = context
            )
            handleSignInResult(result)
        } catch (e: GetCredentialException) {
            e.printStackTrace()
            null
        }
    }

    private fun handleSignInResult(result: GetCredentialResponse): String? {
        val credential = result.credential
        if (credential is CustomCredential && credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
            return try {
                val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                googleIdTokenCredential.idToken
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
        return null
    }
}

@Composable
actual fun rememberGoogleAuthClient(): GoogleAuthClient {
    val context = LocalContext.current
    return remember(context) {
        AndroidGoogleAuthClient(context)
    }
}
