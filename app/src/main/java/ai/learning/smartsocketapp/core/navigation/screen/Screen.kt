package ai.learning.smartsocketapp.core.navigation.screen

import androidx.navigation.NamedNavArgument

sealed class Screen(
    val route: String,
    val navArguments: List<NamedNavArgument> = emptyList()
)
