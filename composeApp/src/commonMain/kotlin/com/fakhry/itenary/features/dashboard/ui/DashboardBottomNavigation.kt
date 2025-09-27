package com.fakhry.itenary.features.dashboard.ui

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.ImageVector
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import com.fakhry.itenary.features.dashboard.navigation.FoodTab
import com.fakhry.itenary.features.dashboard.navigation.HistoryTab
import com.fakhry.itenary.features.dashboard.navigation.ItineraryTab

private data class TabItem(
    val title: String,
    val icon: ImageVector?,
    val tab: Tab,
)

@Composable
fun DashboardBottomNavigation() {
    val navigator = LocalTabNavigator.current
    val items = remember {
        listOf(
            TabItem("Itinerary", icon = null, tab = ItineraryTab),
            TabItem("Food", icon = null, tab = FoodTab),
            TabItem("History", icon = null, tab = HistoryTab),
        )
    }
    NavigationBar {
        items.forEach { item ->
            val selected = navigator.current == item.tab
            NavigationBarItem(
                selected = selected,
                onClick = { navigator.current = item.tab },
                icon = {
                    // TODO: Provide multiplatform icon resources
                    item.icon?.let { Icon(it, contentDescription = item.title) }
                },
                label = { Text(item.title) }
            )
        }
    }
}
