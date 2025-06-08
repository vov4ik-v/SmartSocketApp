package ai.learning.smartsocketapp.core.mapper

import ai.learning.smartsocketapp.core.model.SensorData
import ai.learning.smartsocketapp.core.model.StatisticData
import ai.learning.smartsocketapp.core.network.response.RemoteSensorData
import ai.learning.smartsocketapp.core.network.response.RemoteStatisticData
import java.time.Instant

fun RemoteSensorData.toDomain(): SensorData {
    return SensorData(
        id = this.id,
        voltage = this.voltage,
        current = this.current,
        state = this.state,
        timestamp = Instant.parse(this.timestamp)
    )
}

fun RemoteStatisticData.toDomain(): StatisticData {
    return StatisticData(
        timestamp = Instant.parse(this.timestamp),
        avgVoltage = this.avgVoltage,
        avgCurrent = this.avgCurrent
    )
}