package com.fakhry.itenary.features.itinerary.results.state

import com.fakhry.itenary.features.itinerary.results.model.StreamingItineraryUiModel

data class ResultItineraryUiState(
    val isLoading: Boolean = false,
    val streamingItinerary: StreamingItineraryUiModel = StreamingItineraryUiModel(),
    val isComplete: Boolean = false,
    val error: String? = null,
    val saveSuccess: Boolean = false,
    val saveConflict: Boolean = false,
    val saveError: String? = null,
)
