package ai.learning.smartsocketapp.core.network.repository

import ai.learning.smartsocketapp.core.mapper.toDomain
import ai.learning.smartsocketapp.core.model.SensorData
import ai.learning.smartsocketapp.core.model.StatisticData
import ai.learning.smartsocketapp.core.network.api.SmartSocketService

class SmartSocketRepositoryImpl(
    private val smartSocketService: SmartSocketService
) : SmartSocketRepository {

    override suspend fun getLatestData(): SensorData {
        return try {
            val response = smartSocketService.getLatestData()
            response.toDomain()
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun toggleRelay(state: Boolean): Boolean {
        return try {
            smartSocketService.toggleRelay(state)
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun getRelay(): Boolean {
        return try {
            smartSocketService.getRelay()
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun getDailyStatistic(): List<StatisticData> {
        return try {
            val response = smartSocketService.getDailyStatistic()
            response.map { it.toDomain() }
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun getWeeklyStatistic(): List<StatisticData> {
        return try {
            val response = smartSocketService.getWeeklyStatistic()
            response.map { it.toDomain() }
        } catch (e: Exception) {
            throw e
        }
    }
}