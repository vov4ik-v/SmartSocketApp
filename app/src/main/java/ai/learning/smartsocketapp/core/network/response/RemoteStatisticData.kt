package ai.learning.smartsocketapp.core.network.response

import kotlinx.serialization.Serializable

@Serializable
data class RemoteStatisticData(
    val timestamp: String,
    val avgVoltage: Double,
    val avgCurrent: Double
)
