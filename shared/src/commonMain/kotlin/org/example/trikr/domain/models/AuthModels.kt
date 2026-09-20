package org.example.trikr.domain.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GoogleLoginRequest(
    @SerialName("id_token") val idToken: String
)

@Serializable
data class AuthResponse(
    val message: String,
    val token: String, // Custom JWT from backend
    val user: User
)

@Serializable
data class User(
    val id: String,
    val email: String,
    val name: String,
    val picture: String = ""
)
