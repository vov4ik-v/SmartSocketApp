package ai.learning.smartsocketapp.feature.statistic

import ai.learning.smartsocketapp.core.model.SensorData
import ai.learning.smartsocketapp.core.model.StatisticData
import ai.learning.smartsocketapp.core.network.repository.SmartSocketRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class StatisticViewModel(
    private val repository: SmartSocketRepository
) : ViewModel() {

    private val _sensorData = MutableStateFlow<SensorData?>(null)
    val sensorData: StateFlow<SensorData?> = _sensorData

    private val _isDailyLoading = MutableStateFlow(true)
    val isDailyLoading: StateFlow<Boolean> = _isDailyLoading

    private val _dailyStats = MutableStateFlow<List<StatisticData>>(emptyList())
    val dailyStats: StateFlow<List<StatisticData>> = _dailyStats

    private val _isWeeklyLoading = MutableStateFlow(true)
    val isWeeklyLoading: StateFlow<Boolean> = _isWeeklyLoading

    private val _weeklyStats = MutableStateFlow<List<StatisticData>>(emptyList())
    val weeklyStats: StateFlow<List<StatisticData>> = _weeklyStats


    init {
        fetchStatistics()
        startSensorDataUpdates()
    }

    private fun fetchStatistics() {
        viewModelScope.launch {
            _isDailyLoading.value = true
            _isWeeklyLoading.value = true
            while (true) {
                try {
                    _dailyStats.value = repository.getDailyStatistic()
                    _weeklyStats.value = repository.getWeeklyStatistic()
                } catch (e: Exception) {
                    _dailyStats.value = emptyList()
                    _weeklyStats.value = emptyList()
                } finally {
                    _isDailyLoading.value = false
                    _isWeeklyLoading.value = false
                }
                delay(5000)
            }
        }
    }

    private fun startSensorDataUpdates() {
        viewModelScope.launch {
            while (true) {
                try {
                    _sensorData.value = repository.getLatestData()
                } catch (e: Exception) {
                }
                delay(5000)
            }
        }
    }
}
