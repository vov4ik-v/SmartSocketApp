package ai.learning.smartsocketapp.core.network.api

import ai.learning.smartsocketapp.core.network.NetworkEndpoints
import ai.learning.smartsocketapp.core.network.response.RemoteSensorData
import ai.learning.smartsocketapp.core.network.response.RemoteStatisticData
import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.http.HttpMethod
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer

class SmartSocketServiceImpl(
    client: HttpClient
) : BaseService(client), SmartSocketService {

    override suspend fun getLatestData(): RemoteSensorData =
        executeRequest<Unit, RemoteSensorData>(
            url = NetworkEndpoints.getLatestData(),
            responseSerializer = RemoteSensorData.serializer(),
            method = HttpMethod.Get,
        ).let { result ->
            when (result) {
                is ApiResult.Success -> result.data
                is ApiResult.Error -> {
                    Log.e(
                        "SmartSocketServiceImpl",
                        "Failed to get latest data: ${result.exception}"
                    )
                    throw result.exception
                }
            }
        }

    override suspend fun toggleRelay(state: Boolean): Boolean =
        executeRequest<Unit, ToggleResponse>(
            url = NetworkEndpoints.toggleRelay(),
            responseSerializer = ToggleResponse.serializer(),
            method = HttpMethod.Post,
            params = mapOf("state" to state.toString())
        ).let { result ->
            when (result) {
                is ApiResult.Success -> true
                is ApiResult.Error -> {
                    Log.e("SmartSocketServiceImpl", "Failed to toggle relay: ${result.exception}")
                    throw result.exception
                }
            }
        }


    override suspend fun getRelay(): Boolean =
        executeRequest<Unit, Boolean>(
            url = NetworkEndpoints.getRelay(),
            responseSerializer = Boolean.serializer(),
            method = HttpMethod.Get,
        ).let { result ->
            when (result) {
                is ApiResult.Success -> result.data
                is ApiResult.Error -> {
                    Log.e(
                        "SmartSocketServiceImpl",
                        "Failed to get relay state: ${result.exception}"
                    )
                    throw result.exception
                }
            }
        }

    override suspend fun getDailyStatistic(): List<RemoteStatisticData> =
        executeRequest<Unit, List<RemoteStatisticData>>(
            url = NetworkEndpoints.getDailyStatistic(),
            responseSerializer = ListSerializer(RemoteStatisticData.serializer()),
            method = HttpMethod.Get,
        ).let { result ->
            when (result) {
                is ApiResult.Success -> result.data
                is ApiResult.Error -> {
                    Log.e(
                        "SmartSocketServiceImpl",
                        "Failed to get daily statistics: ${result.exception}"
                    )
                    throw result.exception
                }
            }
        }

    override suspend fun getWeeklyStatistic(): List<RemoteStatisticData> =
        executeRequest<Unit, List<RemoteStatisticData>>(
            url = NetworkEndpoints.getWeeklyStatistic(),
            responseSerializer = ListSerializer(RemoteStatisticData.serializer()),
            method = HttpMethod.Get,
        ).let { result ->
            when (result) {
                is ApiResult.Success -> result.data
                is ApiResult.Error -> {
                    Log.e(
                        "SmartSocketServiceImpl",
                        "Failed to get weekly statistics: ${result.exception}"
                    )
                    throw result.exception
                }
            }
        }


}

@Serializable
data class ToggleResponse(
    val message: String
)
