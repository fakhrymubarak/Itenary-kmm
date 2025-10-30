package com.fakhry.itenary.features.dashboard.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.EggAlt
import androidx.compose.material.icons.outlined.Bookmark
import androidx.compose.material.icons.outlined.EggAlt
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material.icons.rounded.Map
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.fakhry.itenary.features.dashboard.food.navigation.FoodRoute
import com.fakhry.itenary.features.dashboard.history.navigation.HistoryResultsRoute
import com.fakhry.itenary.features.dashboard.itinerary.navigation.ReqStreamItineraryRoute
import com.fakhry.itenary.features.dashboard.navigation.Route

sealed class BottomNavItem(
    val route: Route,
    val title: String,
    val iconActive: ImageVector,
    val iconInactive: ImageVector,
) {
    object Location : BottomNavItem(
        route = ReqStreamItineraryRoute,
        title = "Location",
        iconActive = Icons.Rounded.Map,
        iconInactive = Icons.Outlined.Map,
    )

    object Food : BottomNavItem(
        route = FoodRoute,
        title = "Food",
        iconActive = Icons.Filled.EggAlt,
        iconInactive = Icons.Outlined.EggAlt,
    )

    object HistoryResults : BottomNavItem(
        route = HistoryResultsRoute,
        title = "History",
        iconActive = Icons.Filled.Bookmark,
        iconInactive = Icons.Outlined.Bookmark,
    )
}

@Composable
fun ItenaryBottomNavigation(navHostController: NavHostController) {
    val items = listOf(BottomNavItem.Location, BottomNavItem.Food, BottomNavItem.HistoryResults)
    val navBackStackEntry by navHostController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination?.route ?: BottomNavItem.Location


    NavigationBar {
        items.forEach { item ->
            val isSelected by remember(currentDestination) {
                derivedStateOf { currentDestination == item.route::class.qualifiedName }
            }
            val icon = if (isSelected) item.iconActive else item.iconInactive

            NavigationBarItem(
                icon = { Icon(icon, contentDescription = item.title) },
                label = { Text(item.title) },
                selected = isSelected,
                onClick = {
                    if (currentDestination == item.route::class.qualifiedName) return@NavigationBarItem

                    navHostController.navigate(item.route) {
                        popUpTo(navHostController.graph.startDestinationId)
                        launchSingleTop = true
                    }
                },
            )
        }
    }
}
