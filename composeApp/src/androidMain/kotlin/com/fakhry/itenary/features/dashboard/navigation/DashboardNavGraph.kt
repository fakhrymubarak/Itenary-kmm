package com.fakhry.itenary.features.dashboard.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.fakhry.itenary.model.ItineraryRequestUiModel
import com.fakhry.itenary.features.dashboard.ui.DashboardScreen
import kotlinx.serialization.Serializable

fun NavGraphBuilder.dashboardNavGraph(
    onNavigateToDetails: (itineraryRequest: ItineraryRequestUiModel) -> Unit,
) {
    composable<DashboardRoute> {
        DashboardScreen(
            navHostController = rememberNavController(),
            onNavigateToDetails = onNavigateToDetails,
        )
    }
}

@Serializable
data object DashboardRoute : Route
