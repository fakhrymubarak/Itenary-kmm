package com.fakhry.itenary.features.dashboard.history.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fakhry.itenary.features.dashboard.history.model.DeleteStatus
import com.fakhry.itenary.features.dashboard.history.model.HistoryItineraryUiModel
import com.fakhry.itenary.features.dashboard.history.state.HistoryResultUiState
import com.fakhry.itenary.features.dashboard.history.utils.formatKeyToTitle
import com.itenary.shared.domain.model.SavedItineraryDomain
import com.itenary.shared.domain.repository.ItineraryCacheRepository
import com.itenary.shared.util.DispatcherProvider
import com.itenary.shared.util.DomainResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HistoryResultsViewModel(
    private val cacheRepo: ItineraryCacheRepository,
    private val dispatcherProvider: DispatcherProvider,
) : ViewModel() {

    private val _fetchState: MutableStateFlow<HistoryResultUiState> = MutableStateFlow(HistoryResultUiState.Loading)
    val fetchState = _fetchState.asStateFlow()

    init {
        loadCachedItineraries()
    }

    fun loadCachedItineraries() {
        viewModelScope.launch(dispatcherProvider.io) {
            _fetchState.update { HistoryResultUiState.Loading }
            when (val result = cacheRepo.getSavedItinerary()) {
                is DomainResult.Success -> {
                    val itineraries = result.data.mapIndexed { index, saved -> saved.toUiModel() }
                    _fetchState.update { HistoryResultUiState.Success(itineraries) }
                }

                is DomainResult.Error -> _fetchState.update {
                    HistoryResultUiState.Error(result.message ?: "Unable to load saved itinerary")
                }

                is DomainResult.NetworkError -> _fetchState.update {
                    HistoryResultUiState.Error("Network error while loading saved itinerary")
                }
            }
        }
    }

    fun requestDeleteItinerary(itinerary: HistoryItineraryUiModel) = viewModelScope.launch(dispatcherProvider.io) {
        val currentState = _fetchState.value as? HistoryResultUiState.Success ?: return@launch
        val newItineraries = currentState.itineraries.map {
            if (itinerary.key == it.key) it.copy(deletionStatus = DeleteStatus.SHOW_ALERT_USER) else it
        }
        _fetchState.update { currentState.copy(itineraries = newItineraries) }
    }

    fun cancelDeleteRequest() = viewModelScope.launch(dispatcherProvider.io) {
        val currentState = _fetchState.value as? HistoryResultUiState.Success ?: return@launch
        val itinerary = currentState.itineraries.firstOrNull { it.deletionStatus == DeleteStatus.SHOW_ALERT_USER }
        val newItineraries = currentState.itineraries.map {
            if (itinerary?.key == it.key) it.copy(deletionStatus = DeleteStatus.CANCELLED) else it
        }
        _fetchState.update { currentState.copy(itineraries = newItineraries) }
    }


    fun confirmDeleteRequest() = viewModelScope.launch(dispatcherProvider.io) {
        val currentState = _fetchState.value as? HistoryResultUiState.Success ?: return@launch
        val itineraryToDelete = currentState.itineraries.firstOrNull {
            it.deletionStatus == DeleteStatus.SHOW_ALERT_USER
        }

        if (itineraryToDelete != null) {
            deleteItinerary(itineraryToDelete.key)
        }
    }

    private fun deleteItinerary(key: String) {
        viewModelScope.launch(dispatcherProvider.io) {
            val currentState = _fetchState.value as? HistoryResultUiState.Success ?: return@launch
            val initialIndex = currentState.itineraries.indexOfFirst { it.key == key }
            if (initialIndex == -1) return@launch
            val removedItem = currentState.itineraries[initialIndex]
            _fetchState.update { _ ->
                val updatedList = currentState.itineraries.toMutableList().apply {
                    val removeIndex = indexOfFirst { it.key == key }
                    if (removeIndex != -1) removeAt(removeIndex)
                }
                currentState.copy(itineraries = updatedList)
            }

            when (val result = cacheRepo.deleteItinerary(key)) {
                is DomainResult.Success -> Unit
                is DomainResult.Error -> {
                    restoreItem(initialIndex, removedItem)
                    _fetchState.update {
                        HistoryResultUiState.Error(result.message ?: "Unable to delete itinerary")
                    }
                }

                is DomainResult.NetworkError -> {
                    restoreItem(initialIndex, removedItem)
                    _fetchState.update {
                        HistoryResultUiState.Error("Unable to delete itinerary")
                    }
                }
            }
        }
    }

    private fun restoreItem(index: Int, item: HistoryItineraryUiModel) {
        _fetchState.update { state ->
            if (state !is HistoryResultUiState.Success) return@update state
            if (state.itineraries.any { it.key == item.key }) return@update state

            val restored = state.itineraries.toMutableList().apply {
                val insertIndex = index.coerceAtMost(size)
                add(insertIndex, item)
            }
            state.copy(itineraries = restored)
        }
    }

    private fun SavedItineraryDomain.toUiModel(): HistoryItineraryUiModel {
        return HistoryItineraryUiModel(
            key = key, title = key.formatKeyToTitle(), totalDays = "${itinerary.days.size} Days"
        )
    }
}
