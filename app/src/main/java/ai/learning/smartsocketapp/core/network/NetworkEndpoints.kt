package ai.learning.smartsocketapp.core.network


//
object NetworkEndpoints {
    private const val BASE_URL = "https://smart-socket-app-dcedd173cf01.herokuapp.com"
    private const val API_VERSION = "api"

    private fun constructUrl(apiVersion: String, path: String): String {
        return "$BASE_URL/$apiVersion/$path"
    }


    fun getLatestData(): String = constructUrl(API_VERSION, "latest")
    fun toggleRelay(): String = constructUrl(API_VERSION, "relay")
    fun getRelay(): String = constructUrl(API_VERSION, "relay")
    fun getDailyStatistic(): String = constructUrl(API_VERSION, "statistics/24h")
    fun getWeeklyStatistic(): String = constructUrl(API_VERSION, "statistics/7d")

}
