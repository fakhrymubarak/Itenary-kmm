package com.fakhry.itenary.features.dashboard.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.EggAlt
import androidx.compose.material.icons.outlined.Bookmark
import androidx.compose.material.icons.outlined.EggAlt
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import com.fakhry.itenary.features.dashboard.food.navigation.FoodRoute
import com.fakhry.itenary.features.dashboard.history.navigation.HistoryResultsRoute
import com.fakhry.itenary.features.dashboard.itinerary.navigation.ReqStreamItineraryRoute
import com.fakhry.itenary.features.dashboard.navigation.Route

sealed class BottomNavItem(
    val route: Route,
    val order: Int,
    val title: String,
    val iconActive: ImageVector,
    val iconInactive: ImageVector,
) {
    object Location : BottomNavItem(
        route = ReqStreamItineraryRoute,
        order = 0,
        title = "Location",
        iconActive = Icons.Rounded.LocationOn,
        iconInactive = Icons.Outlined.LocationOn,
    )

    object Food : BottomNavItem(
        route = FoodRoute,
        order = 1,
        title = "Food",
        iconActive = Icons.Filled.EggAlt,
        iconInactive = Icons.Outlined.EggAlt,
    )

    object HistoryResults : BottomNavItem(
        route = HistoryResultsRoute,
        order = 2,
        title = "History",
        iconActive = Icons.Filled.Bookmark,
        iconInactive = Icons.Outlined.Bookmark,
    )
}

@Composable
fun ItenaryBottomNavigation(navHostController: NavHostController) {
    val items = listOf(BottomNavItem.Location, BottomNavItem.Food, BottomNavItem.HistoryResults)
    val startDestination = BottomNavItem.Location
    var selectedDestination by rememberSaveable { mutableIntStateOf(startDestination.order) }

    NavigationBar {
        items.forEach { item ->
            val isSelected = selectedDestination == item.order
            val icon = if (isSelected) item.iconActive else item.iconInactive
            NavigationBarItem(
                icon = { Icon(icon, contentDescription = item.title) },
                label = { Text(item.title) },
                selected = isSelected,
                onClick = {
                    navHostController.navigate(item.route)
                    selectedDestination = item.order
                },
            )
        }
    }
}
