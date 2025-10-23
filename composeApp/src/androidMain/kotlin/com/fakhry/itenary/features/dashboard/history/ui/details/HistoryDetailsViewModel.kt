package com.fakhry.itenary.features.dashboard.history.ui.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fakhry.itenary.features.dashboard.history.state.HistoryDetailsUiState
import com.fakhry.itenary.features.dashboard.history.utils.formatKeyToTitle
import com.fakhry.itenary.features.itinerary.results.model.StreamingDayUiModel
import com.fakhry.itenary.features.itinerary.results.model.toUiModel
import com.itenary.shared.domain.model.SavedItineraryDomain
import com.itenary.shared.domain.repository.ItineraryStreamRepository
import com.itenary.shared.util.DispatcherProvider
import com.itenary.shared.util.DomainResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HistoryDetailsViewModel(
    private val repository: ItineraryStreamRepository,
    private val dispatcherProvider: DispatcherProvider,
    private val key: String,
) : ViewModel() {

    private val _state = MutableStateFlow(HistoryDetailsUiState())
    val state: StateFlow<HistoryDetailsUiState> = _state.asStateFlow()

    init {
        loadItinerary()
    }

    fun loadItinerary() {
        viewModelScope.launch(dispatcherProvider.io) {
            _state.update { it.copy(isLoading = true, error = null) }
            when (val result = repository.getSavedItineraryByKey(key)) {
                is DomainResult.Success -> _state.update { result.data.toUiState() }
                is DomainResult.Error -> _state.update {
                    it.copy(isLoading = false, error = result.message ?: "Unable to load itinerary details")
                }

                is DomainResult.NetworkError -> _state.update {
                    it.copy(isLoading = false, error = "Network error while loading itinerary details")
                }
            }
        }
    }

    private fun SavedItineraryDomain.toUiState(): HistoryDetailsUiState {
        val daysUi = itinerary.days.map { day ->
            StreamingDayUiModel(
                dayIndex = day.dayIndex,
                dayLabel = day.dayLabel,
                items = day.items.map { it.toUiModel() }.toMutableList(),
                isComplete = true
            )
        }

        return HistoryDetailsUiState(
            isLoading = false,
            title = key.formatKeyToTitle(),
            days = daysUi,
            error = null
        )
    }
}
