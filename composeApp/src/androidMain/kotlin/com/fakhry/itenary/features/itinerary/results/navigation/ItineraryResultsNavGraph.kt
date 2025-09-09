package com.fakhry.itenary.features.itinerary.results.navigation

import android.net.Uri
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.fakhry.itenary.commons.model.ItineraryRequestUiModel
import com.fakhry.itenary.features.itinerary.results.ui.ResultStreamItineraryScreen
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlin.reflect.typeOf

fun NavGraphBuilder.itineraryResultsNavGraph(
    onBackPressed: () -> Unit,
) = composable<ItineraryResultsRoute>(
    typeMap = mapOf(
        typeOf<ItineraryRequestUiModel>() to CustomNavType.ItineraryRequest,
    )
) { backstackEntry ->

    val backstackEntryRoute = backstackEntry.toRoute<ItineraryResultsRoute>()
    ResultStreamItineraryScreen(
        itineraryRequestUiModel = backstackEntryRoute.itineraryRequest,
        onNavigateBack = onBackPressed,
    )
}

fun NavController.itineraryResultsNavController(
    itineraryRequest: ItineraryRequestUiModel,
) = navigate(route = ItineraryResultsRoute(itineraryRequest))

@Serializable
data class ItineraryResultsRoute(
    val itineraryRequest: ItineraryRequestUiModel,
)

object CustomNavType {
    val ItineraryRequest = object : NavType<ItineraryRequestUiModel>(
        isNullableAllowed = false
    ) {
        override fun get(bundle: Bundle, key: String): ItineraryRequestUiModel? {
            return Json.decodeFromString(bundle.getString(key) ?: return null)
        }

        override fun parseValue(value: String): ItineraryRequestUiModel {
            return Json.decodeFromString(Uri.decode(value))
        }

        override fun serializeAsValue(value: ItineraryRequestUiModel): String {
            return Uri.encode(Json.encodeToString(value))
        }

        override fun put(bundle: Bundle, key: String, value: ItineraryRequestUiModel) {
            bundle.putString(key, Json.encodeToString(value))
        }
    }
}
