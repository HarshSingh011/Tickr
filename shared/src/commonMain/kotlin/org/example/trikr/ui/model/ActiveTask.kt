package org.example.trikr.ui.model

data class ActiveTask(
    val id: Int,
    val name: String,
    val isCompleted: Boolean,
    val timeElapsedSeconds: Int
)
