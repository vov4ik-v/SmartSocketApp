package ai.learning.smartsocketapp.core.model

import java.time.Instant

data class SensorData(
    val id: String?,
    val voltage: Double,
    val current: Double,
    val state: Boolean,
    val timestamp: Instant
)