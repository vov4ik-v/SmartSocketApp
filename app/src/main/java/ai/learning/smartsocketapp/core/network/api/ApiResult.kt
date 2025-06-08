package ai.learning.smartsocketapp.core.network.api

/**
 * A sealed class representing the result of an API call.
 */
sealed class ApiResult<out T> {
    /**
     * Represents a successful API response containing data of type [T].
     */
    data class Success<out T>(val data: T) : ApiResult<T>()

    /**
     * Represents an error response containing an [Exception].
     */
    data class Error(val exception: Exception) : ApiResult<Nothing>()
}