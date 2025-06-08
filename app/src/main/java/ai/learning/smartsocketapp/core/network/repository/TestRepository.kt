package ai.learning.smartsocketapp.core.network.repository

import ai.learning.smartsocketapp.core.network.response.UserResponse

interface TestRepository {
    suspend fun getUsers(): List<UserResponse>
}