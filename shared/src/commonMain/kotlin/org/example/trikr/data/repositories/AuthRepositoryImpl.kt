package org.example.trikr.data.repositories

import org.example.trikr.data.network.TickrApiClient
import org.example.trikr.data.storage.TokenStorage
import org.example.trikr.domain.repositories.AuthRepository

class AuthRepositoryImpl(
    private val apiClient: TickrApiClient,
    private val tokenStorage: TokenStorage
) : AuthRepository {

    override suspend fun loginWithGoogle(idToken: String): Result<String> {
        val result = apiClient.loginWithGoogle(idToken)
        return result.map { response ->
            // Save the custom JWT returned from our backend
            tokenStorage.saveToken(response.token)
            response.token
        }.recoverCatching { error ->
            throw error
        }
    }

    override fun getSavedToken(): String? {
        return tokenStorage.getToken()
    }

    override fun logout() {
        tokenStorage.clearToken()
    }
}
