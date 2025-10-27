package com.fakhry.itenary.features.dashboard.history.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fakhry.itenary.features.dashboard.history.utils.formatKeyToTitle
import com.itenary.shared.domain.model.SavedItineraryDomain
import com.itenary.shared.domain.repository.ItineraryCacheRepository
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
    val key: String = "",
    val title: String = "",
    val totalDays: String = "",
)

class HistoryResultsViewModel(
    private val cacheRepo: ItineraryCacheRepository,
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
            when (val result = cacheRepo.getSavedItinerary()) {
                is DomainResult.Success -> {
                    val itineraries = result.data.mapIndexed { index, saved -> saved.toUiModel() }
                    _state.update {
                        it.copy(isLoading = false, itineraries = itineraries, error = null)
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

    private fun SavedItineraryDomain.toUiModel(): HistoryItineraryUiModel {
        return HistoryItineraryUiModel(
            key = key, title = key.formatKeyToTitle(), totalDays = "${itinerary.days.size} Days"
        )
    }
}
