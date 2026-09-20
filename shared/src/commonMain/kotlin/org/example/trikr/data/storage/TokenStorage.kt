package org.example.trikr.data.storage

import com.russhwolf.settings.Settings

class TokenStorage {
    private val settings: Settings = Settings()

    companion object {
        private const val KEY_JWT_TOKEN = "jwt_auth_token"
    }

    fun saveToken(token: String) {
        settings.putString(KEY_JWT_TOKEN, token)
    }

    fun getToken(): String? {
        val token = settings.getString(KEY_JWT_TOKEN, "")
        return if (token.isNotEmpty()) token else null
    }

    fun clearToken() {
        settings.remove(KEY_JWT_TOKEN)
    }
}
