package com.fakhry.itenary.features.dashboard.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.fakhry.itenary.commons.model.ItineraryRequestUiModel
import com.fakhry.itenary.features.dashboard.food.navigation.foodNavGraph
import com.fakhry.itenary.features.dashboard.history.navigation.historyResultsNavGraph
import com.fakhry.itenary.features.dashboard.itinerary.navigation.ReqStreamItineraryRoute
import com.fakhry.itenary.features.dashboard.itinerary.navigation.reqStreamItineraryNavGraph
import com.fakhry.itenary.features.dashboard.ui.DashboardViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun NavigationBarNavGraph(
    navHostController: NavHostController,
    innerPadding: PaddingValues,
    dashboardViewModel: DashboardViewModel = koinViewModel(),
    onNavigateToDetails: (ItineraryRequestUiModel) -> Unit,
) {
    NavHost(
        modifier = Modifier.padding(innerPadding),
        navController = navHostController,
        startDestination = ReqStreamItineraryRoute,
    ) {
        reqStreamItineraryNavGraph(
            dashboardViewModel = dashboardViewModel,
            onNavigateToDetails = onNavigateToDetails,
        )
        foodNavGraph(onNavigateBack = navHostController::popBackStack)
        historyResultsNavGraph(onNavigateBack = navHostController::popBackStack)
    }
}
