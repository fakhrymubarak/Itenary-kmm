package com.fakhry.itenary.features.dashboard.itinerary.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.fakhry.itenary.commons.model.ItineraryRequestUiModel
import com.fakhry.itenary.features.dashboard.itinerary.ui.RequestStreamItineraryScreen
import com.fakhry.itenary.features.dashboard.navigation.Route
import com.fakhry.itenary.features.dashboard.ui.DashboardViewModel
import kotlinx.serialization.Serializable

fun NavGraphBuilder.reqStreamItineraryNavGraph(
    dashboardViewModel: DashboardViewModel,
    onNavigateToDetails: (ItineraryRequestUiModel) -> Unit,
) = composable<ReqStreamItineraryRoute> { backstackEntry ->
    RequestStreamItineraryScreen(
        viewModel = dashboardViewModel,
        onNavigateToDetails = onNavigateToDetails,
    )
}

fun NavController.reqStreamItineraryNavController() = navigate(route = ReqStreamItineraryRoute)

@Serializable
data object ReqStreamItineraryRoute : Route
