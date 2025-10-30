package com.fakhry.itenary.features.dashboard.history.state

import com.fakhry.itenary.features.itinerary.results.model.StreamingDayUiModel

data class HistoryDetailsUiState(
    val isLoading: Boolean = true,
    val title: String = "",
    val days: List<StreamingDayUiModel> = emptyList(),
    val error: String? = null,
)
