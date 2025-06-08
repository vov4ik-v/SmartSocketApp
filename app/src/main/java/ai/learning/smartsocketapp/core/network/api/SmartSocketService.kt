package ai.learning.smartsocketapp.core.network.api

import ai.learning.smartsocketapp.core.network.response.RemoteSensorData
import ai.learning.smartsocketapp.core.network.response.RemoteStatisticData

interface SmartSocketService {
    suspend fun getLatestData(): RemoteSensorData
    suspend fun toggleRelay(state: Boolean): Boolean
    suspend fun getRelay() : Boolean
    suspend fun getDailyStatistic(): List<RemoteStatisticData>
    suspend fun getWeeklyStatistic(): List<RemoteStatisticData>

}