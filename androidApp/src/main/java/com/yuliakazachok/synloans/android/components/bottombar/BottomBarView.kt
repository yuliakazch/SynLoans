package com.yuliakazachok.synloans.android.components.bottombar

import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.yuliakazachok.synloans.android.core.CoreBottomScreen
import com.yuliakazachok.synloans.android.core.NavigationKeys.PROFILE
import com.yuliakazachok.synloans.android.core.NavigationKeys.PROFILE_TAB
import com.yuliakazachok.synloans.android.core.NavigationKeys.REQUESTS
import com.yuliakazachok.synloans.android.core.NavigationKeys.REQUESTS_TAB

@Composable
fun BottomBarView(
    tabs: List<CoreBottomScreen>,
    navController: NavHostController,
    currentDestination: NavDestination?,
    currentRoute: String?,
) {
    BottomNavigation(
        backgroundColor = MaterialTheme.colors.background,
        elevation = 0.dp,
    ) {
        tabs.forEach { screen ->
            BottomNavigationItem(
                icon = screen.icon,
                label = { Text(stringResource(screen.stringId)) },
                selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                selectedContentColor = MaterialTheme.colors.primary,
                unselectedContentColor = Color.Gray,
                onClick = {
                    val availableRoute = (screen.route == REQUESTS_TAB && currentRoute != REQUESTS)
                            || (screen.route == PROFILE_TAB && currentRoute != PROFILE)

                    if (availableRoute) {
                        navController.navigate(screen.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            )
        }
    }
}