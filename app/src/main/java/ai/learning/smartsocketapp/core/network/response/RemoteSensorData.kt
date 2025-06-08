package ai.learning.smartsocketapp.core.network.response

import kotlinx.serialization.Serializable
import java.time.Instant

@Serializable
data class RemoteSensorData(
    val id: String?,
    val voltage: Double,
    val current: Double,
    val state: Boolean,
    val timestamp: String
)