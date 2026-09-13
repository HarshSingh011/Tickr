package org.example.trikr.alarm.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.example.trikr.alarm.domain.model.Alarm
import org.example.trikr.alarm.domain.repository.AlarmRepository

class AlarmRepositoryImpl : AlarmRepository {
    // In-memory cache for prototyping
    private val alarms = MutableStateFlow<List<Alarm>>(emptyList())

    override fun getAlarms(): Flow<List<Alarm>> {
        return alarms.asStateFlow()
    }

    override suspend fun saveAlarm(alarm: Alarm) {
        alarms.update { currentList ->
            val index = currentList.indexOfFirst { it.id == alarm.id }
            if (index != -1) {
                // Update existing
                currentList.toMutableList().apply { this[index] = alarm }
            } else {
                // Add new
                currentList + alarm
            }
        }
    }

    override suspend fun deleteAlarm(alarmId: String) {
        alarms.update { currentList ->
            currentList.filter { it.id != alarmId }
        }
    }
}
