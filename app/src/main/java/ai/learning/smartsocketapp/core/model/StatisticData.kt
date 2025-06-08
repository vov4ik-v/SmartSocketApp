package ai.learning.smartsocketapp.core.model

import java.time.Instant

data class StatisticData(
    val timestamp: Instant,
    val avgVoltage: Double,
    val avgCurrent: Double
)
