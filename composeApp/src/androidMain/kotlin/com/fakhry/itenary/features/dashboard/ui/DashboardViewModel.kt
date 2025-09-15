package com.fakhry.itenary.features.dashboard.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fakhry.itenary.commons.model.DayRequestUiModel
import com.fakhry.itenary.commons.model.ItineraryRequestUiModel
import com.itenary.shared.domain.model.BannerDomain
import com.itenary.shared.domain.repository.BannerRepository
import com.itenary.shared.domain.repository.DomainResult
import com.itenary.shared.util.DispatcherProvider
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import java.time.LocalTime
import java.time.format.DateTimeFormatter

enum class ValidationPlaceResult {
    INIT, VALID, INVALID_CANNOT_EMPTY, INVALID_LENGTH_TOO_SHORT, INVALID_LENGTH_TOO_LONG,
}

sealed class UiState<out T>() {
    object Loading : UiState<Nothing>()
    object NetworkError : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Error(val message: String? = null) : UiState<Nothing>()
}

data class DashboardState(
    val itineraryRequest: ItineraryRequestUiModel = ItineraryRequestUiModel(days = listOf(DayRequestUiModel())),
    val validationPlaceResult: ValidationPlaceResult = ValidationPlaceResult.INIT,
    val banners: UiState<List<BannerDomain>> = UiState.Loading,
)

sealed class DashboardSideEffect {
    data class NavigateToResults(val request: ItineraryRequestUiModel) : DashboardSideEffect()
    data class ShowError(val message: String) : DashboardSideEffect()
}

class DashboardViewModel(
    private val bannerRepository: BannerRepository,
    private val dispatcher: DispatcherProvider,
) : ViewModel(), ContainerHost<DashboardState, DashboardSideEffect> {

    private val timeFormatter by lazy { DateTimeFormatter.ofPattern("HH:mm") }
    override val container = container<DashboardState, DashboardSideEffect>(initialState = DashboardState())

    fun updatePlace(place: String) = intent {
        reduce {
            state.copy(
                itineraryRequest = state.itineraryRequest.copy(place = place),
                validationPlaceResult = validatePlace(place)
            )
        }
    }

    fun updateDayTime(dayIndex: Int, day: DayRequestUiModel) = intent {
        val currentDays = state.itineraryRequest.days.toMutableList()
        if (dayIndex < currentDays.size) {
            val fixed = day.enforceTimeWindow()
            currentDays[dayIndex] = fixed
            reduce {
                state.copy(itineraryRequest = state.itineraryRequest.copy(days = currentDays))
            }
        }
    }

    fun addDay() = intent {
        val currentDays = state.itineraryRequest.days.toMutableList()
        val newDayNumber = currentDays.size + 1
        currentDays.add(DayRequestUiModel(day = newDayNumber))
        reduce { state.copy(itineraryRequest = state.itineraryRequest.copy(days = currentDays)) }
    }

    fun removeDay(dayIndex: Int) = intent {
        if (state.itineraryRequest.days.size > 1) {
            val currentDays = state.itineraryRequest.days.toMutableList()
            currentDays.removeAt(dayIndex)
            val renumberedDays = currentDays.mapIndexed { index, dayRequest -> dayRequest.copy(day = index + 1) }
            reduce {
                state.copy(itineraryRequest = state.itineraryRequest.copy(days = renumberedDays))
            }
        }
    }

    fun generateItinerary() = intent {
        postSideEffect(DashboardSideEffect.NavigateToResults(state.itineraryRequest))
    }

    fun loadBanners() = intent {
        reduce { state.copy(banners = UiState.Loading) }
        viewModelScope.launch(dispatcher.io) {
            val bannerResult = bannerRepository.getBanners()
            reduce {
                state.copy(banners = bannerResult.mapDomainResultToUiState { it })
            }
        }
    }

    private fun DayRequestUiModel.enforceTimeWindow(minMinutes: Long = 1L): DayRequestUiModel {
        val start = LocalTime.parse(startTime, timeFormatter)
        val end = LocalTime.parse(endTime, timeFormatter)
        return if (start.isBefore(end)) {
            this
        } else {
            val adjustedEnd = start.plusMinutes(minMinutes)
            copy(endTime = adjustedEnd.format(timeFormatter))
        }
    }

    private fun validatePlace(place: String): ValidationPlaceResult {
        val trimmedPlace = place.trim()
        return when {
            trimmedPlace.isBlank() -> ValidationPlaceResult.INVALID_CANNOT_EMPTY
            trimmedPlace.length < 2 -> ValidationPlaceResult.INVALID_LENGTH_TOO_SHORT
            trimmedPlace.length > 100 -> ValidationPlaceResult.INVALID_LENGTH_TOO_LONG
            else -> ValidationPlaceResult.VALID
        }
    }
}

private fun <T, R> DomainResult<T>.mapDomainResultToUiState(
    mapper: (T) -> R,
): UiState<R> {
    return when (this) {
        is DomainResult.Error -> UiState.Error(message)
        DomainResult.NetworkError -> UiState.NetworkError
        is DomainResult.Success<T> -> UiState.Success(mapper(data))
    }
}

