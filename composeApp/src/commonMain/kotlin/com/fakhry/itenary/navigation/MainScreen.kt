package com.fakhry.itenary.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import com.fakhry.itenary.features.dashboard.navigation.DashboardTab

@Composable
fun MainScreen() {
    Navigator(DashboardTab) { navigator ->
        TabNavigator(DashboardTab) { CurrentTab() }
    }
}
