package org.example.trikr.domain.repositories

interface AuthRepository {
    suspend fun loginWithGoogle(idToken: String): Result<String>
    fun getSavedToken(): String?
    fun logout()
}
