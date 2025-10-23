package com.fakhry.itenary.features.dashboard.history.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.fakhry.itenary.features.dashboard.history.ui.HistoryResultsScreen
import com.fakhry.itenary.features.dashboard.navigation.Route
import kotlinx.serialization.Serializable

fun NavGraphBuilder.historyResultsNavGraph(
    onNavigateBack: () -> Unit,
    onNavigateToHistoryDetails: (String) -> Unit,
) = composable<HistoryResultsRoute> { backstackEntry ->
    HistoryResultsScreen(
        onNavigateBack = onNavigateBack,
        onItinerarySelected = onNavigateToHistoryDetails,
    )
}

@Serializable
data object HistoryResultsRoute : Route
