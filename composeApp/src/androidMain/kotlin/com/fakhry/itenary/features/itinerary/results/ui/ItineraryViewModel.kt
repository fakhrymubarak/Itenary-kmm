package com.fakhry.itenary.features.itinerary.results.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fakhry.itenary.commons.model.ItineraryRequestUiModel
import com.fakhry.itenary.commons.model.toListDayRequest
import com.fakhry.itenary.features.itinerary.results.model.StreamingDayUiModel
import com.fakhry.itenary.features.itinerary.results.model.TimelineItemUiModel
import com.fakhry.itenary.features.itinerary.results.model.toUiModel
import com.fakhry.itenary.features.itinerary.results.state.ResultItineraryUiState
import com.itenary.shared.domain.model.DayDomain
import com.itenary.shared.domain.model.ItineraryDomain
import com.itenary.shared.domain.model.ItineraryRequest
import com.itenary.shared.domain.model.PriceDomain
import com.itenary.shared.domain.model.StreamingEvent
import com.itenary.shared.domain.model.TimelineItemDomain
import com.itenary.shared.domain.model.TravelFromPrevDomain
import com.itenary.shared.domain.repository.ItineraryCacheRepository
import com.itenary.shared.domain.usecase.StreamItineraryUseCase
import com.itenary.shared.util.DispatcherProvider
import com.itenary.shared.util.DomainResult
import com.itenary.shared.util.ERR_DB_INSERT_CONFLICT
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class ItineraryViewModel(
    private val streamItineraryUseCase: StreamItineraryUseCase,
    private val dispatcher: DispatcherProvider,
    private val cacheRepo: ItineraryCacheRepository,
) : ViewModel() {

    // Non-conflated stream of UI states
    private val _streamState = MutableSharedFlow<ResultItineraryUiState>(
        replay = 0, extraBufferCapacity = 64, onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val streamingUiState: SharedFlow<ResultItineraryUiState> = _streamState

    // Keep the latest snapshot since SharedFlow doesn't hold a value
    @Volatile
    private var latest: ResultItineraryUiState = ResultItineraryUiState()
    val latestState: ResultItineraryUiState get() = latest

    private lateinit var place: String
    private var streamJob: Job? = null

    private fun emit(update: (ResultItineraryUiState) -> ResultItineraryUiState) {
        latest = update(latest)
        _streamState.tryEmit(latest)
    }

    fun streamItineraryGeneration(request: ItineraryRequestUiModel) {
        streamJob = viewModelScope.launch(dispatcher.io) {
            emit { it.copy(isLoading = true, error = null) }

            place = request.place
            val itineraryRequest = ItineraryRequest(
                place = request.place,
                days = request.days.toListDayRequest(),
                language = request.language,
                llm = request.llm
            )

            try {
                streamItineraryUseCase(itineraryRequest).collect { event ->
                    if (latest.isLoading) emit { it.copy(isLoading = false, error = null) }

                    when (event) {
                        is StreamingEvent.Init -> Unit
                        is StreamingEvent.DayStart -> emit { prev ->
                            val cur = prev.streamingItinerary
                            val updatedDays = cur.days.toMutableList().apply {
                                add(
                                    StreamingDayUiModel(
                                        dayIndex = event.dayIndex, dayLabel = event.dayLabel
                                    )
                                )
                            }
                            prev.copy(
                                streamingItinerary = cur.copy(days = updatedDays)
                            )
                        }

                        is StreamingEvent.TimelineItemAdded -> emit { prev ->
                            val cur = prev.streamingItinerary
                            val updatedDays = cur.days.toMutableList()
                            val day = updatedDays.getOrNull(event.dayIndex)
                            if (day != null) {
                                val updatedItems = day.items.toMutableList().apply {
                                    add(event.item.toUiModel())
                                }
                                updatedDays[event.dayIndex] = day.copy(items = updatedItems)
                            }
                            prev.copy(streamingItinerary = cur.copy(days = updatedDays))
                        }

                        is StreamingEvent.DayComplete -> emit { prev ->
                            val cur = prev.streamingItinerary
                            val updatedDays = cur.days.toMutableList()
                            if (event.dayIndex in updatedDays.indices) {
                                val d = updatedDays[event.dayIndex]
                                updatedDays[event.dayIndex] = d.copy(isComplete = true)
                            }
                            prev.copy(streamingItinerary = cur.copy(days = updatedDays))
                        }

                        is StreamingEvent.Complete -> emit { prev ->
                            prev.copy(
                                isLoading = false, streamingItinerary = prev.streamingItinerary.copy(isComplete = true)
                            )
                        }

                        is StreamingEvent.Error -> emit { prev ->
                            prev.copy(isLoading = false, error = event.message)
                        }
                    }
                }
            } catch (e: Exception) {
                emit { prev ->
                    prev.copy(isLoading = false, error = e.message ?: "Streaming failed")
                }
            }
        }
    }

    fun saveToLocalStorage() {
        val domain = currentItineraryDomain() ?: return

        viewModelScope.launch(dispatcher.io) {

            val key = place.mapToKey()
            when (val result = cacheRepo.cacheItinerary(key, domain)) {
                is DomainResult.Success -> emit {
                    it.copy(saveSuccess = true, saveError = null, saveConflict = false)
                }

                is DomainResult.Error -> emit {
                    // handle insert DB failed due to already existing key
                    if (result.code == ERR_DB_INSERT_CONFLICT) {
                        it.copy(saveConflict = true, saveError = null)
                    } else {
                        it.copy(saveError = result.message ?: "Saving itinerary failed", saveConflict = false)
                    }
                }

                is DomainResult.NetworkError -> emit {
                    it.copy(saveError = "Network error while saving", saveConflict = false)
                }
            }
        }
    }

    private fun String.mapToKey() = this.replace(", ", "_").lowercase()

    fun consumeSaveResult() {
        emit { it.copy(saveSuccess = false) }
    }

    fun consumeSaveError() {
        emit { it.copy(saveError = null) }
    }

    fun consumeSaveConflict() {
        emit { it.copy(saveConflict = false, saveError = null) }
    }

    fun overwriteExistingItinerary() {
        val domain = currentItineraryDomain() ?: return
        viewModelScope.launch(dispatcher.io) {
            val key = place.mapToKey()
            when (val result = cacheRepo.overwriteItinerary(key, domain)) {
                is DomainResult.Success -> emit {
                    it.copy(saveSuccess = true, saveConflict = false, saveError = null)
                }

                is DomainResult.Error -> emit {
                    it.copy(saveError = result.message ?: "Save failed", saveConflict = false)
                }

                is DomainResult.NetworkError -> emit {
                    it.copy(saveError = "Network error while saving", saveConflict = false)
                }
            }
        }
    }

    fun keepBothItinerary() {
        val domain = currentItineraryDomain() ?: return
        viewModelScope.launch(dispatcher.io) {
            val key = place.mapToKey()
            when (val result = cacheRepo.cacheItineraryCopy(key, domain)) {
                is DomainResult.Success -> emit {
                    it.copy(saveSuccess = true, saveConflict = false, saveError = null)
                }

                is DomainResult.Error -> emit {
                    it.copy(saveError = result.message ?: "Save failed", saveConflict = false)
                }

                is DomainResult.NetworkError -> emit {
                    it.copy(saveError = "Network error while saving", saveConflict = false)
                }
            }
        }
    }

    private fun TimelineItemUiModel.toDomain(): TimelineItemDomain = TimelineItemDomain(
        timeEnd = timeEnd,
        timeStart = timeStart,
        lng = lng,
        lat = lat,
        expenseEstimation = PriceDomain(),
        pinpointMapsUrl = pinpointMapsUrl,
        name = name,
        shortDesc = shortDesc,
        category = category,
        travelFromPrev = TravelFromPrevDomain(
            mode = travelFromPrev.mode,
            modeStr = "",
            // Lost details in UI model; store defaults
            fareEstimate = PriceDomain(),
            distanceKm = 0f,
            durationMin = 0,
        ),
        source = source,
        confidence = confidence,
    )

    override fun onCleared() {
        streamJob?.cancel()
        super.onCleared()
    }

    private fun currentItineraryDomain(): ItineraryDomain? {
        val snapshot = latestState.streamingItinerary
        if (snapshot.days.isEmpty()) return null

        return ItineraryDomain(
            days = snapshot.days.map { day ->
                DayDomain(
                    dayIndex = day.dayIndex,
                    dayLabel = day.dayLabel,
                    items = day.items.map { it.toDomain() }.toMutableList()
                )
            }.toMutableList()
        )
    }
}
