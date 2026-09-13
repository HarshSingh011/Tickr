package org.example.trikr.alarm.domain.usecase

import org.example.trikr.alarm.domain.model.Alarm
import org.example.trikr.alarm.domain.repository.AlarmRepository

class SaveAlarmUseCase(private val repository: AlarmRepository) {
    suspend operator fun invoke(alarm: Alarm) {
        if (alarm.title.isBlank()) {
            throw IllegalArgumentException("Alarm title cannot be blank")
        }
        repository.saveAlarm(alarm)
    }
}
