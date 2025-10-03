package com.fakhry.itenary.features.dashboard.viewmodel

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.fakhry.itenary.features.dashboard.model.DashboardSideEffect
import com.fakhry.itenary.features.dashboard.model.DashboardState
import com.fakhry.itenary.features.dashboard.model.UiState
import com.fakhry.itenary.features.dashboard.model.ValidationPlaceResult
import com.fakhry.itenary.features.dashboard.model.mapDomainResultToUiState
import com.fakhry.itenary.model.DayRequestUiModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.itenary.shared.domain.repository.BannerRepository
import com.itenary.shared.util.DispatcherProvider
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
class DashboardScreenModel(
    private val bannerRepository: BannerRepository,
    private val dispatcher: DispatcherProvider,
) : ScreenModel {

    private val _uiState = MutableStateFlow(DashboardState())
    val uiState: StateFlow<DashboardState> = _uiState.asStateFlow()

    private val _sideEffect = MutableSharedFlow<DashboardSideEffect>(extraBufferCapacity = 8, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    val sideEffect: SharedFlow<DashboardSideEffect> = _sideEffect.asSharedFlow()

    init {
        loadBanners()
    }

    fun updatePlace(place: String) {
        _uiState.update { state ->
            state.copy(
                itineraryRequest = state.itineraryRequest.copy(place = place),
                validationPlaceResult = validatePlace(place)
            )
        }
    }

    fun updateDayTime(dayIndex: Int, day: DayRequestUiModel) {
        _uiState.update { state ->
            val currentDays = state.itineraryRequest.days.toMutableList()
            if (dayIndex < currentDays.size) {
                currentDays[dayIndex] = day.enforceTimeWindow()
            }
            state.copy(itineraryRequest = state.itineraryRequest.copy(days = currentDays))
        }
    }

    fun addDay(currentDay: DayRequestUiModel) {
        _uiState.update { state ->
            val updatedDays = state.itineraryRequest.days + currentDay
            state.copy(itineraryRequest = state.itineraryRequest.copy(days = updatedDays))
        }
    }

    fun removeDay(updatedList: List<DayRequestUiModel>) {
        _uiState.update { state ->
            if (state.itineraryRequest.days.size > 1) {
                state.copy(itineraryRequest = state.itineraryRequest.copy(days = updatedList))
            } else {
                state
            }
        }
    }

    fun generateItinerary() {
        _uiState.value.itineraryRequest.let { request ->
            _sideEffect.tryEmit(DashboardSideEffect.NavigateToResults(request))
        }
    }

    fun loadBanners() {
        _uiState.update { it.copy(banners = UiState.Loading) }
        screenModelScope.launch(dispatcher.io) {
            val result = bannerRepository.getBanners()
            _uiState.update { state ->
                state.copy(banners = result.mapDomainResultToUiState { it })
            }
        }
    }

    private fun DayRequestUiModel.enforceTimeWindow(minMinutes: Int = 1): DayRequestUiModel {
        val startMinutes = startTime.toMinutesOrNull() ?: return this
        val endMinutes = endTime.toMinutesOrNull() ?: return this
        return if (startMinutes < endMinutes) {
            this
        } else {
            val adjustedEnd = (startMinutes + minMinutes).coerceAtMost(MAX_DAY_MINUTES)
            copy(endTime = adjustedEnd.toTimeString())
        }
    }

    private fun validatePlace(place: String): ValidationPlaceResult {
        val trimmed = place.trim()
        return when {
            trimmed.isBlank() -> ValidationPlaceResult.INVALID_CANNOT_EMPTY
            trimmed.length < 2 -> ValidationPlaceResult.INVALID_LENGTH_TOO_SHORT
            trimmed.length > 100 -> ValidationPlaceResult.INVALID_LENGTH_TOO_LONG
            else -> ValidationPlaceResult.VALID
        }
    }
}

fun DashboardState.topBannerRequest() = itineraryRequest
fun DashboardState.isGenerateEnabled() = validationPlaceResult == ValidationPlaceResult.VALID
fun DashboardState.withUpdatedPlace(place: String, validation: ValidationPlaceResult) = copy(
    itineraryRequest = itineraryRequest.copy(place = place),
    validationPlaceResult = validation
)
fun DashboardState.withUpdatedDays(days: List<DayRequestUiModel>) = copy(
    itineraryRequest = itineraryRequest.copy(days = days)
)

private const val MAX_DAY_MINUTES = 23 * 60 + 59

private fun String.toMinutesOrNull(): Int? {
    val parts = split(":")
    if (parts.size != 2) return null
    val hour = parts[0].toIntOrNull() ?: return null
    val minute = parts[1].toIntOrNull() ?: return null
    if (hour !in 0..23 || minute !in 0..59) return null
    return hour * 60 + minute
}

private fun Int.toTimeString(): String {
    val normalized = coerceIn(0, MAX_DAY_MINUTES)
    val hour = normalized / 60
    val minute = normalized % 60
    val hourString = if (hour < 10) "0$hour" else hour.toString()
    val minuteString = if (minute < 10) "0$minute" else minute.toString()
    return "$hourString:$minuteString"
}
