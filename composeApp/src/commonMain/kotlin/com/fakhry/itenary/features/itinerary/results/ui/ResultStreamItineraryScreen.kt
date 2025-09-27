package com.fakhry.itenary.features.itinerary.results.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.fakhry.itenary.features.itinerary.results.model.StreamingDayUiModel
import com.fakhry.itenary.features.itinerary.results.model.StreamingItineraryUiModel
import com.fakhry.itenary.features.itinerary.results.model.toUiModel
import com.fakhry.itenary.model.ItineraryRequestUiModel
import com.fakhry.itenary.model.toDomain
import com.fakhry.itenary.theme.Spacing
import com.itenary.shared.domain.model.StreamingEvent
import com.itenary.shared.domain.usecase.StreamItineraryUseCase

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultStreamItineraryScreen(
    itineraryRequest: ItineraryRequestUiModel,
    onNavigateBack: () -> Unit,
    streamItineraryUseCase: StreamItineraryUseCase,
) {
    var state by remember { mutableStateOf(ResultItineraryUiState(isLoading = true)) }

    LaunchedEffect(itineraryRequest) {
        streamItineraryUseCase(itineraryRequest.toDomain()).collect { event ->
            state = state.reduce(event)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Itinerary Results") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                state.isLoading -> LoadingState()
                state.error != null -> ErrorState(message = state.error, onNavigateBack = onNavigateBack)
                state.itinerary.days.isNotEmpty() -> StreamingDaysSection(state.itinerary)
            }
        }
    }
}

@Composable
private fun LoadingState() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator()
        Spacer(modifier = Modifier.height(Spacing.md))
        Text(text = "Generating your itinerary...", style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
private fun ErrorState(message: String?, onNavigateBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(Spacing.md),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "❌ Error", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(Spacing.md))
        Text(text = message ?: "Something went wrong", style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(Spacing.lg))
        Button(onClick = onNavigateBack) { Text("Go Back") }
    }
}

data class ResultItineraryUiState(
    val isLoading: Boolean = false,
    val itinerary: StreamingItineraryUiModel = StreamingItineraryUiModel(),
    val error: String? = null,
)

private fun ResultItineraryUiState.reduce(event: StreamingEvent): ResultItineraryUiState =
    when (event) {
        StreamingEvent.Init -> copy(isLoading = true, error = null)
        is StreamingEvent.DayStart -> copy(
            isLoading = false,
            itinerary = itinerary.copy(
                days = itinerary.days + StreamingDayUiModel(
                    dayIndex = event.dayIndex,
                    dayLabel = event.dayLabel
                )
            )
        )

        is StreamingEvent.TimelineItemAdded -> copy(
            isLoading = false,
            itinerary = itinerary.copy(
                days = itinerary.days.mapIndexed { index, day ->
                    if (index == event.dayIndex) {
                        day.copy(items = day.items + event.item.toUiModel())
                    } else {
                        day
                    }
                }
            )
        )

        is StreamingEvent.DayComplete -> copy(
            isLoading = false,
            itinerary = itinerary.copy(
                days = itinerary.days.mapIndexed { index, day ->
                    if (index == event.dayIndex) day.copy(isComplete = true) else day
                }
            )
        )

        StreamingEvent.Complete -> copy(isLoading = false, itinerary = itinerary.copy(isComplete = true))
        is StreamingEvent.Error -> copy(isLoading = false, error = event.message)
    }
