package com.fakhry.itenary.features.dashboard.food.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.fakhry.itenary.features.dashboard.food.ui.FoodScreen
import com.fakhry.itenary.features.dashboard.navigation.Route
import kotlinx.serialization.Serializable

fun NavGraphBuilder.foodNavGraph(
    onNavigateBack: () -> Unit,
) = composable<FoodRoute> { backstackEntry ->
    FoodScreen(
        onNavigateBack = onNavigateBack,
    )
}

fun NavController.foodNavController() = navigate(route = FoodRoute)

@Serializable
data object FoodRoute : Route
