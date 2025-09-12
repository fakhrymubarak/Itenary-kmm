package com.fakhry.itenary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fakhry.itenary.commons.model.ItineraryRequestUiModel
import com.fakhry.itenary.commons.model.toListDayRequest
import com.fakhry.itenary.features.itinerary.results.model.TimelineItemUiModel
import com.fakhry.itenary.features.itinerary.results.model.toUiModel
import com.itenary.shared.domain.model.ItineraryRequest
import com.itenary.shared.domain.model.StreamingDay
import com.itenary.shared.domain.model.StreamingEvent
import com.itenary.shared.domain.model.StreamingItinerary
import com.itenary.shared.domain.usecase.StreamItineraryUseCase
import com.itenary.shared.util.DispatcherProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ResultItineraryState(
    val isLoading: Boolean = false,
    val streamingItinerary: StreamingItinerary = StreamingItinerary(),
    val isComplete: Boolean = false,
    val error: String? = null,
)

data class StreamingDayUiModel(
    val dayIndex: Int,
    val dayLabel: String,
    val items: MutableList<TimelineItemUiModel> = mutableListOf(),
    val isComplete: Boolean = false,
)

data class StreamingItineraryUiModel(
    val days: MutableList<StreamingDayUiModel> = mutableListOf(),
    val isComplete: Boolean = false,
)

data class ResultItineraryUiState(
    val isLoading: Boolean = false,
    val streamingItinerary: StreamingItineraryUiModel = StreamingItineraryUiModel(),
    val isComplete: Boolean = false,
    val error: String? = null,
)

class ItineraryViewModel(
    private val streamItineraryUseCase: StreamItineraryUseCase,
    private val dispatcher: DispatcherProvider,
) : ViewModel() {

    private val _streamingState = MutableStateFlow(ResultItineraryState())
    val streamingState: StateFlow<ResultItineraryState> = _streamingState.asStateFlow()

    private val _streamingUiState = MutableStateFlow(ResultItineraryUiState())
    val streamingUiState: StateFlow<ResultItineraryUiState> = _streamingUiState.asStateFlow()

    private fun convertToUiState(domainState: ResultItineraryState): ResultItineraryUiState {
        val uiDays =
            domainState
                .streamingItinerary
                .days
                .map { day ->
                    StreamingDayUiModel(
                        dayIndex = day.dayIndex,
                        dayLabel = day.dayLabel,
                        items = day.items.map { it.toUiModel() }.toMutableList(),
                        isComplete = day.isComplete
                    )
                }
                .toMutableList()

        return ResultItineraryUiState(
            isLoading = domainState.isLoading,
            streamingItinerary =
                StreamingItineraryUiModel(
                    days = uiDays,
                    isComplete = domainState.streamingItinerary.isComplete
                ),
            isComplete = domainState.isComplete,
            error = domainState.error
        )
    }

    fun streamItineraryGeneration(request: ItineraryRequestUiModel) {
        viewModelScope.launch(dispatcher.io) {
            _streamingState.value = _streamingState.value.copy(isLoading = true)
            _streamingUiState.value = convertToUiState(_streamingState.value)

            val itineraryRequest =
                ItineraryRequest(
                    place = request.place,
                    days = request.days.toListDayRequest(),
                    language = request.language,
                    llm = request.llm
                )

            // try catch catch potential parsing errors
            try {
                streamItineraryUseCase(itineraryRequest).collect { event ->
                    println("Trace event $event")
                    when (event) {
                        is StreamingEvent.Init -> {}
                        is StreamingEvent.DayStart -> {
                            val currentStreaming = _streamingState.value.streamingItinerary
                            val newDay =
                                StreamingDay(
                                    dayIndex = event.dayIndex,
                                    dayLabel = event.dayLabel
                                )
                            currentStreaming.days.add(newDay)
                            _streamingState.value =
                                _streamingState.value.copy(
                                    streamingItinerary = currentStreaming
                                )
                            _streamingUiState.value = convertToUiState(_streamingState.value)
                        }

                        is StreamingEvent.TimelineItemAdded -> {
                            val currentStreaming = _streamingState.value.streamingItinerary
                            val day = currentStreaming.days.getOrNull(event.dayIndex)
                            day?.items?.add(event.item)

                            _streamingState.value =
                                _streamingState.value.copy(
                                    streamingItinerary = currentStreaming
                                )
                            _streamingUiState.value = convertToUiState(_streamingState.value)
                        }

                        is StreamingEvent.DayComplete -> {
                            val currentStreaming = _streamingState.value.streamingItinerary
                            val dayIndex = event.dayIndex
                            if (dayIndex >= 0) {
                                currentStreaming.days[dayIndex] =
                                    currentStreaming.days[dayIndex].copy(isComplete = true)
                            }
                            _streamingState.value =
                                _streamingState.value.copy(
                                    streamingItinerary = currentStreaming
                                )
                            _streamingUiState.value = convertToUiState(_streamingState.value)
                        }

                        is StreamingEvent.Complete -> {
                            _streamingState.value =
                                _streamingState.value.copy(
                                    isLoading = false,
                                    streamingItinerary =
                                        _streamingState.value.streamingItinerary.copy(
                                            isComplete = true
                                        )
                                )
                            _streamingUiState.value = convertToUiState(_streamingState.value)
                        }

                        is StreamingEvent.Error -> {
                            _streamingState.value =
                                _streamingState.value.copy(
                                    isLoading = false,
                                    error = event.message
                                )
                            _streamingUiState.value = convertToUiState(_streamingState.value)
                        }
                    }
                }
            } catch (e: Exception) {
                _streamingState.value =
                    _streamingState.value.copy(
                        isLoading = false,
                        error = e.message ?: "Streaming failed"
                    )
                _streamingUiState.value = convertToUiState(_streamingState.value)
            }
        }
    }
}
