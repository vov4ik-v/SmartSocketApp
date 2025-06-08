package ai.learning.smartsocketapp.core.network.api

import ai.learning.smartsocketapp.core.network.response.UserResponse

interface TestService {
    suspend fun getUsers(): List<UserResponse>
}