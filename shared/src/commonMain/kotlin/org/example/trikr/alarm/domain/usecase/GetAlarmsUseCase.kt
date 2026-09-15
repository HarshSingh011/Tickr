package org.example.trikr.alarm.domain.usecase

import kotlinx.coroutines.flow.Flow
import org.example.trikr.alarm.domain.model.Alarm
import org.example.trikr.alarm.domain.repository.AlarmRepository

class GetAlarmsUseCase(private val repository: AlarmRepository) {
    operator fun invoke(): Flow<List<Alarm>> {
        return repository.getAlarms()
    }
}
