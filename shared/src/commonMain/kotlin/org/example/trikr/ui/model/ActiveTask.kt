package org.example.trikr.ui.model

data class ActiveTask(
    val id: String,
    val name: String,
    val isCompleted: Boolean,
    val timeElapsedSeconds: Int
)
