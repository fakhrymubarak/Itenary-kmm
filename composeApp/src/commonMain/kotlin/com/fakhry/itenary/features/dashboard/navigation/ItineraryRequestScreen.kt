package com.fakhry.itenary.features.dashboard.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.fakhry.itenary.features.dashboard.ui.RequestStreamItineraryScreen
import com.fakhry.itenary.features.itinerary.results.navigation.ItineraryResultScreen

object ItineraryRequestScreen : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        RequestStreamItineraryScreen(onNavigateToDetails = { request ->
            navigator.push(ItineraryResultScreen(request))
        })
    }
}

