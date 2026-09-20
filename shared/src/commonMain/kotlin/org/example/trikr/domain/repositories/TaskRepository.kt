package org.example.trikr.domain.repositories

import org.example.trikr.domain.models.TaskDto

interface TaskRepository {
    suspend fun startTask(name: String): Result<Unit>
    suspend fun stopTask(id: String): Result<Unit>
    suspend fun getTodaysTasks(): Result<List<TaskDto>>
}
