package ai.learning.smartsocketapp.core.network.api

import ai.learning.smartsocketapp.core.network.NetworkConfig
import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.request.header
import io.ktor.client.request.request
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.http.contentType
import kotlinx.serialization.KSerializer
import kotlinx.serialization.serializer
import java.net.URLEncoder
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


/**
 * Base class for API services, providing common functionality for making HTTP requests.
 */
abstract class BaseService(
    protected val client: HttpClient,

) {
    /**
     * Executes an HTTP request and returns an [ApiResult] containing the response data or an error.
     *
     * @param url The URL to request.
     * @param responseSerializer The serializer for the response data.
     * @param method The HTTP method to use (default is GET).
     * @param body The request body, if any.
     * @param headers Additional headers to include in the request.
     * @param params Query parameters to include in the request.
     */
    protected suspend inline fun <reified T, reified R> executeRequest(
        url: String,
        responseSerializer: KSerializer<R>,
        method: HttpMethod = HttpMethod.Get,
        body: T? = null,
        headers: Map<String, String> = emptyMap(),
        params: Map<String, String> = emptyMap()
    ): ApiResult<R> = try {
        val response: HttpResponse = client.request(url) {
            this.method = method

            headers.forEach { (key, value) -> header(key, value) }

            Log.d("BaseService","Request URL: $url")
            if (body != null) {
                contentType(ContentType.Application.Json)
                setBody(
                    NetworkConfig.jsonConfiguration.encodeToString(
                        NetworkConfig.jsonConfiguration.serializersModule.serializer(),
                        body
                    )
                )
            }
            url {
                params.forEach { (key, value) -> parameters.append(key, value) }
            }
        }
        val responseBody = response.bodyAsText()
        ApiResult.Success(
            NetworkConfig.jsonConfiguration.decodeFromString(
                responseSerializer,
                responseBody
            )
        )
    } catch (e: Exception) {
        Log.e("BaseService","API request failed: $e")
        ApiResult.Error(e)
    }


    /**
     * Gets the current time formatted and URL-encoded.
     *
     * @return The current time as a URL-encoded string.
     */
    protected fun getCurrentEncodedTime(): String = URLEncoder.encode(
        LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
        "UTF-8"
    )
}
