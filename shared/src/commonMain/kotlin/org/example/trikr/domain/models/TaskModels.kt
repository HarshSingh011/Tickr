package org.example.trikr.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class StartTaskRequest(
    val name: String
)

@Serializable
data class TaskDto(
    val id: String,
    val user_id: String,
    val name: String,
    val status: String,
    val start_time: String,
    val end_time: String? = null,
    val duration_seconds: Int
)

@Serializable
data class TaskHistoryResponse(
    val message: String,
    val tasks: List<TaskDto>
)

@Serializable
data class DailyStatDto(
    val date: String,
    val name: String,
    val total_duration_seconds: Int
)

@Serializable
data class DailyStatsResponse(
    val message: String,
    val daily_stats: List<DailyStatDto>
)
