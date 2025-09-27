package com.fakhry.itenary.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import com.fakhry.itenary.features.dashboard.navigation.ItineraryTab
import com.fakhry.itenary.features.dashboard.ui.DashboardBottomNavigation

@Composable
fun MainScreen() {
    TabNavigator(ItineraryTab) { _ ->
        Scaffold(bottomBar = { DashboardBottomNavigation() }) { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding)) {
                CurrentTab()
            }
        }
    }
}
