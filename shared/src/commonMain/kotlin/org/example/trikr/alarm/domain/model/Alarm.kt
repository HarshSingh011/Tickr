package org.example.trikr.alarm.domain.model

data class Alarm(
    val id: String,
    val title: String,
    val timeH: Int,
    val timeM: Int,
    val isDaily: Boolean,
    val frequencyMinutes: Int,
    val isActive: Boolean
)
