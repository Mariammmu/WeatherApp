package com.mariammuhammad.climate.navigation

import android.content.Context
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.mariammuhammad.climate.R

data class NavigationItem(
    val title: String,
    val icon: ImageVector,
    val route: NavigationRoute
)

@Composable
fun getBottomNavigationList(context: Context): List<NavigationItem> {
    return listOf(
        NavigationItem(
            title = stringResource(R.string.home),
            icon = Icons.Default.Home,
            route = NavigationRoute.HomeScreen
        ),
        NavigationItem(
            title = stringResource(R.string.alerts),
            icon = Icons.Default.Notifications,
            route = NavigationRoute.WeatherAlertScreen
        ),
        NavigationItem(
            title = stringResource(R.string.favorites),
            icon = Icons.Default.Favorite,
            route = NavigationRoute.FavoriteScreen
        ),
        NavigationItem(
            title = stringResource(R.string.settings),
            icon = Icons.Default.Settings,
            route = NavigationRoute.SettingScreen
        )
    )
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry.value?.destination

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        modifier = Modifier.height(80.dp)
    ) {
        getBottomNavigationList(LocalContext.current).forEach { item ->
            val isSelected = currentDestination?.route == item.route::class.java.simpleName

            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    navController.navigate(item.route::class.java.simpleName) {
                        // Avoid multiple copies of the same destination
                        launchSingleTop = true
                        // Restore state when reselecting a previously selected item
                        restoreState = true
                        // Pop up to the start destination of the graph to
                        // avoid building up a large stack of destinations
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                    }
                },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.title
                    )
                },
                label = {
                    Text(text = item.title)
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = colorResource(R.color.violet),//MaterialTheme.colorScheme.primary,
                    unselectedIconColor =  colorResource(R.color.background),//MaterialTheme.colorScheme.onSurfaceVariant,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    unselectedTextColor = colorResource(R.color.background),//MaterialTheme.colorScheme.onSurfaceVariant,
                    indicatorColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    }
}