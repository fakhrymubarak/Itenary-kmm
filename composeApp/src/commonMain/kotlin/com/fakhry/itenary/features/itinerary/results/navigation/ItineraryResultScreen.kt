package com.fakhry.itenary.features.itinerary.results.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.fakhry.itenary.features.itinerary.results.ui.ResultStreamItineraryScreen
import com.fakhry.itenary.model.ItineraryRequestUiModel
import com.itenary.shared.domain.usecase.StreamItineraryUseCase
import org.koin.compose.koinInject

data class ItineraryResultScreen(
    val request: ItineraryRequestUiModel,
) : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val streamItineraryUseCase: StreamItineraryUseCase = koinInject()

        ResultStreamItineraryScreen(
            itineraryRequest = request,
            onNavigateBack = { navigator.pop() },
            streamItineraryUseCase = streamItineraryUseCase,
        )
    }
}

