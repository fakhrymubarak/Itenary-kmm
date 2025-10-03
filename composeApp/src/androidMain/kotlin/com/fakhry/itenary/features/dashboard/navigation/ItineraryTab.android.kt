package com.fakhry.itenary.features.dashboard.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.fakhry.itenary.features.dashboard.navigation.ItineraryRequestScreen

actual object ItineraryTab : Tab {
    override val options: TabOptions
        @Composable get() = tabOptions(0u, "Itinerary")

    @Composable
    override fun Content() {
        Navigator(ItineraryRequestScreen)
    }
}
