package org.example.trikr.alarm.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.example.trikr.alarm.domain.model.Alarm
import org.example.trikr.alarm.domain.usecase.GetAlarmsUseCase
import org.example.trikr.alarm.domain.usecase.SaveAlarmUseCase
import kotlin.random.Random

data class AlarmUiState(
    val title: String = "",
    val timeH: Int = 5,
    val timeM: Int = 0,
    val isDaily: Boolean = true,
    val frequencyMinutes: Int = 5
)

class AlarmViewModel(
    private val getAlarmsUseCase: GetAlarmsUseCase,
    private val saveAlarmUseCase: SaveAlarmUseCase
) : ViewModel() {

    val alarms: StateFlow<List<Alarm>> = getAlarmsUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _uiState = MutableStateFlow(AlarmUiState())
    val uiState: StateFlow<AlarmUiState> = _uiState.asStateFlow()

    fun updateTitle(title: String) {
        _uiState.update { it.copy(title = title) }
    }

    fun updateTime(h: Int, m: Int) {
        _uiState.update { it.copy(timeH = h, timeM = m) }
    }

    fun updateIsDaily(isDaily: Boolean) {
        _uiState.update { it.copy(isDaily = isDaily) }
    }

    fun updateFrequency(minutes: Int) {
        _uiState.update { it.copy(frequencyMinutes = minutes) }
    }

    fun saveAlarm() {
        viewModelScope.launch {
            val state = _uiState.value
            if (state.title.isNotBlank()) {
                val newAlarm = Alarm(
                    id = Random.nextLong().toString(),
                    title = state.title,
                    timeH = state.timeH,
                    timeM = state.timeM,
                    isDaily = state.isDaily,
                    frequencyMinutes = state.frequencyMinutes,
                    isActive = true
                )
                saveAlarmUseCase(newAlarm)
                // Reset form
                _uiState.value = AlarmUiState()
            }
        }
    }
}
