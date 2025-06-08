package ai.learning.smartsocketapp.core.network.api

import ai.learning.smartsocketapp.core.network.response.UserResponse
import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.http.HttpMethod
import kotlinx.serialization.builtins.ListSerializer

class TestServiceImpl(
    client: HttpClient
) : BaseService(client), TestService{

    override suspend fun getUsers(): List<UserResponse> =
        executeRequest<Unit, List<UserResponse>>(
            url = "https://smart-socket-app-dcedd173cf01.herokuapp.com/api/users",
            responseSerializer = ListSerializer(UserResponse.serializer()),
            method = HttpMethod.Get,
        ).let { result ->
            when (result) {
                is ApiResult.Success -> result.data
                is ApiResult.Error -> {
                    Log.e("TestServiceImpl","Failed to get users: ${result.exception}")
                    emptyList()
                }
            }
        }
}