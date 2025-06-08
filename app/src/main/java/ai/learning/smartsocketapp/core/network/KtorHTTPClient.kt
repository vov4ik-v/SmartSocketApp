package ai.learning.smartsocketapp.core.network

import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json


object KtorClientBuilder {
    fun build(): HttpClient = HttpClient {
        install(ContentNegotiation) {
            json(NetworkConfig.jsonConfiguration)
        }
        install(Logging) {
            level = LogLevel.ALL
        }
        install(HttpTimeout) {
            connectTimeoutMillis = 10000
            socketTimeoutMillis = 30000
        }
    }
}
object NetworkConfig {
    val jsonConfiguration = Json {
        ignoreUnknownKeys = true
        isLenient = true
        encodeDefaults = true
        allowStructuredMapKeys = true
    }
}
