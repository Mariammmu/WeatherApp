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

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry.value?.destination

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        modifier = Modifier.height(80.dp)
    ) {
        NavigationBarItem(
            selected = false,
            onClick = {

                navController.navigate(NavigationRoute.HomeScreen()){
                    popUpTo<NavigationRoute.HomeScreen>{
                        inclusive = true
                    }
                }
            },
            icon = {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = stringResource(R.string.home)
                )
            },
            label = {
                Text(text = stringResource(R.string.home))
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = colorResource(R.color.violet),//MaterialTheme.colorScheme.primary,
                unselectedIconColor =  colorResource(R.color.background),//MaterialTheme.colorScheme.onSurfaceVariant,
                selectedTextColor = MaterialTheme.colorScheme.primary,
                unselectedTextColor = colorResource(R.color.background),//MaterialTheme.colorScheme.onSurfaceVariant,
                indicatorColor = MaterialTheme.colorScheme.surface
            )
        )

        NavigationBarItem(
            selected = false,
            onClick = {
                navController.navigate(NavigationRoute.FavoriteScreen){
                    popUpTo<NavigationRoute.HomeScreen>{} // true
                }
            },
            icon = {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = stringResource(R.string.favorites)
                )
            },
            label = {
                Text(text = stringResource(R.string.favorites))
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = colorResource(R.color.violet),//MaterialTheme.colorScheme.primary,
                unselectedIconColor =  colorResource(R.color.background),//MaterialTheme.colorScheme.onSurfaceVariant,
                selectedTextColor = MaterialTheme.colorScheme.primary,
                unselectedTextColor = colorResource(R.color.background),//MaterialTheme.colorScheme.onSurfaceVariant,
                indicatorColor = MaterialTheme.colorScheme.surface
            )
        )

        NavigationBarItem(
            selected = false,
            onClick = {
                navController.navigate(NavigationRoute.SettingScreen){
                    popUpTo<NavigationRoute.HomeScreen>{}
                }
            },
            icon = {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = stringResource(R.string.settings)
                )
            },
            label = {
                Text(text = stringResource(R.string.settings))
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = colorResource(R.color.violet),//MaterialTheme.colorScheme.primary,
                unselectedIconColor =  colorResource(R.color.background),//MaterialTheme.colorScheme.onSurfaceVariant,
                selectedTextColor = MaterialTheme.colorScheme.primary,
                unselectedTextColor = colorResource(R.color.background),//MaterialTheme.colorScheme.onSurfaceVariant,
                indicatorColor = MaterialTheme.colorScheme.surface
            )
        )

        NavigationBarItem(
            selected = false,
            onClick = {
                navController.navigate(NavigationRoute.WeatherAlertScreen){
                    popUpTo<NavigationRoute.HomeScreen>{}
                }
            },
            icon = {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = stringResource(R.string.alerts)
                )
            },
            label = {
                Text(text = stringResource(R.string.alerts))
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