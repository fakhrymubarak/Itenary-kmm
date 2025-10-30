package com.fakhry.itenary.features.dashboard.history.state

import com.fakhry.itenary.features.dashboard.history.model.HistoryItineraryUiModel

sealed class HistoryResultUiState {
    data object Loading : HistoryResultUiState()
    data class Success(val itineraries: List<HistoryItineraryUiModel>) : HistoryResultUiState()
    data class Error(val message: String) : HistoryResultUiState()
}
