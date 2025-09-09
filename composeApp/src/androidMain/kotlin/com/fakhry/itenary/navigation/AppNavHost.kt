package com.fakhry.itenary.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.fakhry.itenary.features.dashboard.navigation.DashboardRoute
import com.fakhry.itenary.features.dashboard.navigation.dashboardNavGraph
import com.fakhry.itenary.features.itinerary.results.navigation.itineraryResultsNavController
import com.fakhry.itenary.features.itinerary.results.navigation.itineraryResultsNavGraph

private const val ANIMATION_DURATION = 300

@Composable
fun AppNavHost(
    navController: NavHostController,
    startDestination: DashboardRoute,
) {

    val enterTransition = slideInHorizontally(
        initialOffsetX = { it },
        animationSpec = tween(ANIMATION_DURATION),
    )
    val exitTransition = slideOutHorizontally(
        targetOffsetX = { -it },
        animationSpec = tween(ANIMATION_DURATION),
    )
    val popEnterTransition = slideInHorizontally(
        initialOffsetX = { -it },
        animationSpec = tween(ANIMATION_DURATION),
    )
    val popExitTransition = slideOutHorizontally(
        targetOffsetX = { it },
        animationSpec = tween(ANIMATION_DURATION),
    )

    NavHost(
        navController = navController,
        startDestination = startDestination,
        enterTransition = { enterTransition },
        exitTransition = { exitTransition },
        popEnterTransition = { popEnterTransition },
        popExitTransition = { popExitTransition },
    ) {
        dashboardNavGraph(onNavigateToDetails = navController::itineraryResultsNavController)
        itineraryResultsNavGraph(navController::popBackStack)
    }
}
