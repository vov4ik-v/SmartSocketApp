package ai.learning.smartsocketapp.core.network.repository

import ai.learning.smartsocketapp.core.network.api.TestService
import ai.learning.smartsocketapp.core.network.response.UserResponse

class TestRepositoryImpl(
    private val testService: TestService
) : TestRepository {
    override suspend fun getUsers(): List<UserResponse> {
        return try {
            val response = testService.getUsers()
            response
        } catch (e: Exception) {
            emptyList()
        }
    }
}