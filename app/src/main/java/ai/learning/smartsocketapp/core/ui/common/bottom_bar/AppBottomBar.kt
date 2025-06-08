package ai.learning.smartsocketapp.core.ui.common.bottom_bar

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun AppBottomBar(
    bottomBarItems: List<TabBarItem>,
    navController: NavController,
    selectedTabIndex: Int,
    onChangeTab: (Int) -> Unit
) {


    NavigationBar(
        containerColor = Color.Transparent,
        tonalElevation = 0.dp
    ) {
        bottomBarItems.forEachIndexed { index, item ->
            val isSelected = selectedTabIndex == index

            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    onChangeTab(index)
                    navController.navigate(item.route)
                },
                icon = {
                    TabBarIconView(
                        isSelected = isSelected,
                        selectedIcon = item.selectedIcon,
                        unselectedIcon = item.unselectedIcon,
                        title = item.title,
                        badgeAmount = item.badgeAmount
                    )
                },
                label = {
                    Text(
                        text = item.title,
                        fontSize = 14.sp,
                        color = Color(
                            0xFF1C1C1C
                        )
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color(
                        0xFF1C1C1C
                    ),
                    unselectedIconColor = Color(0xFF1C1C1C),
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TabBarIconView(
    isSelected: Boolean,
    selectedIcon: ImageVector,
    unselectedIcon: ImageVector,
    title: String,
    badgeAmount: Int? = null
) {
    val icon = if (isSelected) selectedIcon else unselectedIcon

    if (badgeAmount != null && badgeAmount > 0) {
        BadgedBox(badge = { Badge { Text("$badgeAmount") } }) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                modifier = Modifier.size(28.dp)
            )
        }
    } else {
        Icon(
            imageVector = icon,
            contentDescription = title,
            modifier = Modifier.size(28.dp)
        )
    }
}



data class TabBarItem(
    val title: String,
    val route: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val badgeAmount: Int? = null
)