package com.fakhry.itenary.features.dashboard.ui

import androidx.lifecycle.ViewModel
import com.fakhry.itenary.commons.model.DayRequestUiModel
import com.fakhry.itenary.commons.model.ItineraryRequestUiModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container

enum class ValidationPlaceResult {
    INIT,
    VALID,
    INVALID_CANNOT_EMPTY,
    INVALID_LENGTH_TOO_SHORT,
    INVALID_LENGTH_TOO_LONG,
}

data class DashboardState(
    val itineraryRequest: ItineraryRequestUiModel = ItineraryRequestUiModel(
        days = listOf(DayRequestUiModel())
    ),
    val validationPlaceResult: ValidationPlaceResult = ValidationPlaceResult.VALID,
)

sealed class DashboardSideEffect {
    data class NavigateToResults(val request: ItineraryRequestUiModel) : DashboardSideEffect()
    data class ShowError(val message: String) : DashboardSideEffect()
}

class DashboardViewModel : ViewModel(), ContainerHost<DashboardState, DashboardSideEffect> {

    override val container = container<DashboardState, DashboardSideEffect>(initialState = DashboardState())

    fun updatePlace(place: String) = intent {
        reduce {
            state.copy(
                itineraryRequest = state.itineraryRequest.copy(place = place),
                validationPlaceResult = validatePlace(place)
            )
        }
    }

    fun updateDayTime(dayIndex: Int, startTime: String, endTime: String, pace: Float) = intent {
        val currentDays = state.itineraryRequest.days.toMutableList()
        if (dayIndex < currentDays.size) {
            currentDays[dayIndex] = currentDays[dayIndex].copy(startTime = startTime, endTime = endTime, pace = pace)
            reduce { state.copy(itineraryRequest = state.itineraryRequest.copy(days = currentDays)) }
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
            val renumberedDays =
                currentDays.mapIndexed { index, dayRequest -> dayRequest.copy(day = index + 1) }
            reduce { state.copy(itineraryRequest = state.itineraryRequest.copy(days = renumberedDays)) }
        }
    }

    fun generateItinerary() = intent {
        try {
            postSideEffect(DashboardSideEffect.NavigateToResults(state.itineraryRequest))
        } catch (e: Exception) {
            postSideEffect(DashboardSideEffect.ShowError(e.message ?: "Unknown error"))
        }
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
