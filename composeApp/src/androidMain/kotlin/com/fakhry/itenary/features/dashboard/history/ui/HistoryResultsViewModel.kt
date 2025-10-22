package com.fakhry.itenary.features.dashboard.history.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fakhry.itenary.features.itinerary.results.model.TimelineItemUiModel
import com.fakhry.itenary.features.itinerary.results.model.toUiModel
import com.itenary.shared.domain.model.ItineraryDomain
import com.itenary.shared.domain.repository.ItineraryStreamRepository
import com.itenary.shared.util.DispatcherProvider
import com.itenary.shared.util.DomainResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class HistoryResultsState(
    val isLoading: Boolean = false,
    val itineraries: List<HistoryItineraryUiModel> = emptyList(),
    val error: String? = null,
)

data class HistoryItineraryUiModel(
    val title: String,
    val days: List<HistoryDayUiModel>,
)

data class HistoryDayUiModel(
    val label: String,
    val items: List<TimelineItemUiModel>,
)

class HistoryResultsViewModel(
    private val repository: ItineraryStreamRepository,
    private val dispatcherProvider: DispatcherProvider,
) : ViewModel() {

    private val _state = MutableStateFlow(HistoryResultsState(isLoading = true))
    val state: StateFlow<HistoryResultsState> = _state.asStateFlow()

    init {
        loadCachedItineraries()
    }

    fun loadCachedItineraries() {
        viewModelScope.launch(dispatcherProvider.io) {
            _state.update { it.copy(isLoading = true, error = null) }
            when (val result = repository.getSavedItinerary()) {
                is DomainResult.Success -> {
                    val itineraries = result.data.mapIndexed { index, itinerary ->
                        itinerary.toUiModel(index)
                    }
                    _state.update {
                        it.copy(
                            isLoading = false,
                            itineraries = itineraries,
                            error = null,
                        )
                    }
                }

                is DomainResult.Error -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            itineraries = emptyList(),
                            error = result.message ?: "Unable to load saved itinerary",
                        )
                    }
                }

                is DomainResult.NetworkError -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            itineraries = emptyList(),
                            error = "Network error while loading saved itinerary",
                        )
                    }
                }
            }
        }
    }

    private fun ItineraryDomain.toUiModel(index: Int): HistoryItineraryUiModel {
        val daysUiModel = days.map { day ->
            HistoryDayUiModel(
                label = day.dayLabel,
                items = day.items.map { it.toUiModel() }
            )
        }
        val title = "Itinerary #${index + 1}"
        return HistoryItineraryUiModel(title = title, days = daysUiModel)
    }
}
