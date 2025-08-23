package com.fakhry.itenary.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import com.fakhry.itenary.navigation.NavRoutes

sealed class BottomNavItem(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Location : BottomNavItem(
        route = NavRoutes.DASHBOARD,
        title = "Location",
        icon = Icons.Default.LocationOn
    )
    
    object Food : BottomNavItem(
        route = "food",
        title = "Food",
        icon = Icons.Default.Menu
    )
    
    object Bookmarks : BottomNavItem(
        route = "bookmarks",
        title = "Bookmarks",
        icon = Icons.Default.Star
    )
}

@Composable
fun ItenaryBottomNavigation(
    currentRoute: String,
    onNavigate: (String) -> Unit
) {
    val items = listOf(
        BottomNavItem.Location,
        BottomNavItem.Food,
        BottomNavItem.Bookmarks
    )
    
    NavigationBar {
        items.forEach { item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.title) },
                label = { Text(item.title) },
                selected = currentRoute == item.route,
                onClick = { onNavigate(item.route) }
            )
        }
    }
}
