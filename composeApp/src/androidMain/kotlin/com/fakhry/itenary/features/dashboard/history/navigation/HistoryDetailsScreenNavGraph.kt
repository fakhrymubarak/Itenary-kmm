package com.fakhry.itenary.features.dashboard.history.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.fakhry.itenary.features.dashboard.navigation.Route
import kotlinx.serialization.Serializable

fun NavGraphBuilder.historyDetailsNavGraph(
    onNavigateBack: () -> Unit,
) = composable<HistoryDetailsRoute> { backstackEntry ->

}

fun NavController.historyDetailsNavController(key: String) = navigate(route = HistoryDetailsRoute(key))

@Serializable
data class HistoryDetailsRoute(
    val key: String
) : Route
