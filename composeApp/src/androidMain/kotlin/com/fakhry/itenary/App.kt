package com.fakhry.itenary

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.fakhry.itenary.navigation.NavRoutes
import com.fakhry.itenary.theme.ItenaryTheme
import com.fakhry.itenary.ui.bookmarks.BookmarksScreen
import com.fakhry.itenary.ui.components.ItenaryBottomNavigation
import com.fakhry.itenary.ui.dashboard.DashboardScreen
import com.fakhry.itenary.ui.food.FoodScreen
import com.fakhry.itenary.ui.results.ItineraryResultScreen
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    ItenaryTheme {
        val navController = rememberNavController()
        var currentRoute by remember { mutableStateOf(NavRoutes.DASHBOARD) }

        val currentRouteString = currentRoute.substringBefore('?')

        // Define which routes should show bottom navigation
        val routesWithBottomNav = setOf(NavRoutes.DASHBOARD, NavRoutes.FOOD, NavRoutes.BOOKMARKS)
        val showBottomNav = currentRouteString in routesWithBottomNav

        Scaffold(
            bottomBar = {
                if (showBottomNav) {
                    ItenaryBottomNavigation(
                        currentRoute = currentRoute,
                        onNavigate = { route ->
                            navController.navigate(route) {
                                popUpTo(navController.graph.startDestinationId) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                            currentRoute = route
                        },
                    )
                }
            },
        ) { paddingValues ->
            NavHost(
                navController = navController,
                startDestination = NavRoutes.DASHBOARD,
                modifier = Modifier.padding(paddingValues)
            ) {
                composable(NavRoutes.DASHBOARD) {
                    DashboardScreen(onNavigateToDetails = { itineraryRequest ->
                        navController.navigate(NavRoutes.ITINERARY_RESULTS)
                    })
                }
                composable(NavRoutes.FOOD) {
                    FoodScreen(onNavigateBack = { navController.popBackStack() })
                }
                composable(NavRoutes.BOOKMARKS) {
                    BookmarksScreen(onNavigateBack = { navController.popBackStack() })
                }

                composable(
                    route = NavRoutes.ITINERARY_RESULTS
                ) { backStackEntry ->

                    ItineraryResultScreen(
                        onNavigateBack = { navController.popBackStack() }
                    )
                }
            }
        }
    }
}
