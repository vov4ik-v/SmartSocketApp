package ai.learning.smartsocketapp

import ai.learning.smartsocketapp.core.navigation.SmartSocketNavGraph
import ai.learning.smartsocketapp.core.navigation.screen.HomeScreens
import ai.learning.smartsocketapp.core.navigation.screen.StatisticScreens
import ai.learning.smartsocketapp.core.ui.common.bottom_bar.AppBottomBar
import ai.learning.smartsocketapp.core.ui.common.bottom_bar.TabBarItem
import ai.learning.smartsocketapp.ui.theme.SmartSocketAppTheme
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material.icons.outlined.PowerSettingsNew
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            val switchTab = TabBarItem(
                title = "Switch",
                route = HomeScreens.HomeScreen.route,
                selectedIcon = Icons.Filled.PowerSettingsNew,
                unselectedIcon = Icons.Outlined.PowerSettingsNew
            )
            val staticTab = TabBarItem(
                title = "Statistic",
                route = StatisticScreens.StatisticScreen.route,
                selectedIcon = Icons.Filled.BarChart,
                unselectedIcon = Icons.Outlined.BarChart
            )

            val bottomBarItems = listOf(switchTab, staticTab)

            var selectedTabIndex by rememberSaveable {
                mutableIntStateOf(0)
            }

            SmartSocketAppTheme {
                val gradientBackground = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF88BBD6),
                        Color(0xFFD8A6C5)
                    )
                )

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(gradientBackground)
                ) {
                    Scaffold(
                        contentWindowInsets = WindowInsets(0, 0, 0, 0),
                        containerColor = Color.Transparent,
                        topBar = {
                            CenterAlignedTopAppBar(
                                title = {
                                    Text(
                                        bottomBarItems[selectedTabIndex].title,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 28.sp
                                    )
                                },
                                colors = TopAppBarDefaults.topAppBarColors(
                                    containerColor = Color.Transparent,
                                    titleContentColor = Color.Black
                                )
                            )
                        },
                        bottomBar = {
                            AppBottomBar(
                                navController = navController,
                                bottomBarItems = bottomBarItems,
                                selectedTabIndex = selectedTabIndex,
                                onChangeTab = { index ->
                                    selectedTabIndex = index
                                }
                            )
                        }
                    ) { innerPadding ->
                        Surface(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding),
                            color = Color.Transparent
                        ) {
                            SmartSocketNavGraph(navHostController = navController)
                        }
                    }
                }
            }


        }
    }
}