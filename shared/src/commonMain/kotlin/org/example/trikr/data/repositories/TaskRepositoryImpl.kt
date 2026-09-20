package org.example.trikr.data.repositories

import org.example.trikr.data.network.TickrApiClient
import org.example.trikr.domain.repositories.TaskRepository

import org.example.trikr.domain.models.TaskDto
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.toInstant
import kotlinx.datetime.LocalDateTime

class TaskRepositoryImpl(
    private val apiClient: TickrApiClient
) : TaskRepository {
    override suspend fun startTask(name: String): Result<Unit> {
        return apiClient.startTask(name)
    }

    override suspend fun stopTask(id: String): Result<Unit> {
        return apiClient.stopTask(id)
    }

    override suspend fun getTodaysTasks(): Result<List<TaskDto>> {
        val currentMoment = Clock.System.now()
        val timeZone = TimeZone.currentSystemDefault()
        val localDate = currentMoment.toLocalDateTime(timeZone).date
        
        val startOfDay = LocalDateTime(localDate.year, localDate.month, localDate.dayOfMonth, 0, 0, 0, 0)
        val endOfDay = LocalDateTime(localDate.year, localDate.month, localDate.dayOfMonth, 23, 59, 59, 999999999)
        
        val startTimeStr = startOfDay.toInstant(timeZone).toString()
        val endTimeStr = endOfDay.toInstant(timeZone).toString()
        
        val result = apiClient.getTasksHistory(startTimeStr, endTimeStr)
        return result.map { it.tasks }
    }
}
