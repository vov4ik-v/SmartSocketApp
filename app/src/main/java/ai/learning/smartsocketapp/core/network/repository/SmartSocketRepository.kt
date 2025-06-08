package ai.learning.smartsocketapp.core.network.repository

import ai.learning.smartsocketapp.core.model.SensorData
import ai.learning.smartsocketapp.core.model.StatisticData

interface SmartSocketRepository {
    suspend fun getLatestData(): SensorData
    suspend fun toggleRelay(state: Boolean): Boolean
    suspend fun getRelay() : Boolean
    suspend fun getDailyStatistic(): List<StatisticData>
    suspend fun getWeeklyStatistic(): List<StatisticData>
}