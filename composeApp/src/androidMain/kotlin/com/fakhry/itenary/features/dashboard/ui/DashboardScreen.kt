package com.fakhry.itenary.features.dashboard.ui

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.fakhry.itenary.commons.model.ItineraryRequestUiModel
import com.fakhry.itenary.features.dashboard.navigation.NavigationBarNavGraph
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    navHostController: NavHostController,
    onNavigateToDetails: (ItineraryRequestUiModel) -> Unit = {},
) {
    val viewModel = koinViewModel<DashboardViewModel>()
    Scaffold(
        bottomBar = {
            ItenaryBottomNavigation(navHostController)
        },
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
    ) { innerPadding ->
        NavigationBarNavGraph(
            navHostController = navHostController,
            innerPadding = innerPadding,
            dashboardViewModel = viewModel,
            onNavigateToDetails = onNavigateToDetails
        )
    }
}
