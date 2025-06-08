package ai.learning.smartsocketapp.core.navigation

import ai.learning.smartsocketapp.core.navigation.screen.HomeScreens.HomeScreen
import ai.learning.smartsocketapp.core.navigation.screen.StatisticScreens.StatisticScreen
import ai.learning.smartsocketapp.feature.home.HomeScreen
import ai.learning.smartsocketapp.feature.home.HomeViewModel
import ai.learning.smartsocketapp.feature.statistic.StatisticScreen
import ai.learning.smartsocketapp.feature.statistic.StatisticViewModel
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import org.koin.androidx.compose.getViewModel

@Composable
fun SmartSocketNavGraph(
    navHostController: NavHostController
) {
    NavHost(
        navController = navHostController,
        startDestination = HomeScreen.route
    ) {
        composable(HomeScreen.route) { backStackEntry ->
            val viewModel = getViewModel<HomeViewModel>(viewModelStoreOwner = backStackEntry)
            HomeScreen(viewModel = viewModel)
        }
        composable(StatisticScreen.route) { backStackEntry ->
            val viewModel = getViewModel<StatisticViewModel>(viewModelStoreOwner = backStackEntry)
            StatisticScreen(viewModel = viewModel)
        }
    }
}
