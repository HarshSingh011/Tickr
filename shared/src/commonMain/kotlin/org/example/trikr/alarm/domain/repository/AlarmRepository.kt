package org.example.trikr.alarm.domain.repository

import kotlinx.coroutines.flow.Flow
import org.example.trikr.alarm.domain.model.Alarm

interface AlarmRepository {
    fun getAlarms(): Flow<List<Alarm>>
    suspend fun saveAlarm(alarm: Alarm)
    suspend fun deleteAlarm(alarmId: String)
}
