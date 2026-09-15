package org.example.trikr

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform