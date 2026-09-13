package org.example.trikr.di

import org.example.trikr.alarm.data.repository.AlarmRepositoryImpl
import org.example.trikr.alarm.domain.usecase.GetAlarmsUseCase
import org.example.trikr.alarm.domain.usecase.SaveAlarmUseCase

object ServiceLocator {
    val alarmRepository by lazy { AlarmRepositoryImpl() }
    val getAlarmsUseCase by lazy { GetAlarmsUseCase(alarmRepository) }
    val saveAlarmUseCase by lazy { SaveAlarmUseCase(alarmRepository) }
}
