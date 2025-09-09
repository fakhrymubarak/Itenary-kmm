package com.fakhry.itenary.features.dashboard.ui

import androidx.lifecycle.ViewModel
import com.fakhry.itenary.commons.model.DayRequestUiModel
import com.fakhry.itenary.commons.model.ItineraryRequestUiModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container

// 1. STATE - Define the state
data class DashboardState(
    val itineraryRequest: ItineraryRequestUiModel =
        ItineraryRequestUiModel(
            place = "",
            days =
                listOf(
                    DayRequestUiModel(
                        day = 1,
                        startTime = "09:00",
                        endTime = "18:00"
                    )
                ),
            language = "English",
            llm = "gemini"
        ),
    val isLoading: Boolean = false,
    val error: String? = null,
    val validationErrors: Map<String, String> = emptyMap(),
)

// 2. SIDE EFFECTS - Define side effects
sealed class DashboardSideEffect {
    data class NavigateToResults(val request: ItineraryRequestUiModel) : DashboardSideEffect()
    data class ShowError(val message: String) : DashboardSideEffect()
    data class ShowSuccess(val message: String) : DashboardSideEffect()
}

class DashboardViewModel : ViewModel(), ContainerHost<DashboardState, DashboardSideEffect> {

    // 3. CONTAINER - Orbit MVI container
    override val container =
        container<DashboardState, DashboardSideEffect>(initialState = DashboardState())

    // 4. INTENTS - User actions using Orbit MVI
    fun updatePlace(place: String) = intent {
        reduce {
            state.copy(
                itineraryRequest = state.itineraryRequest.copy(place = place),
                validationErrors = state.validationErrors - "place"
            )
        }
    }

    fun updateDayTime(dayIndex: Int, startTime: String, endTime: String) = intent {
        val currentDays = state.itineraryRequest.days.toMutableList()
        if (dayIndex < currentDays.size) {
            currentDays[dayIndex] = currentDays[dayIndex].copy(startTime = startTime, endTime = endTime)
            reduce { state.copy(itineraryRequest = state.itineraryRequest.copy(days = currentDays)) }
        }
    }

    fun addDay() = intent {
        val currentDays = state.itineraryRequest.days.toMutableList()
        val newDayNumber = currentDays.size + 1
        currentDays.add(
            DayRequestUiModel(
                day = newDayNumber,
                startTime = "09:00",
                endTime = "18:00",
            )
        )
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

    fun updateLanguage(language: String) = intent {
        reduce { state.copy(itineraryRequest = state.itineraryRequest.copy(language = language)) }
    }

    fun updateLlm(llm: String) = intent {
        reduce { state.copy(itineraryRequest = state.itineraryRequest.copy(llm = llm)) }
    }

    fun generateItinerary() = intent {
        // Validate input
        val validationErrors = validateInput()
        if (validationErrors.isNotEmpty()) {
            reduce { state.copy(validationErrors = validationErrors) }
            postSideEffect(DashboardSideEffect.ShowError("Please fix validation errors"))
            return@intent
        }

        // Set loading state
        reduce { state.copy(isLoading = true) }

        try {
            // Emit navigation side effect
            postSideEffect(DashboardSideEffect.NavigateToResults(state.itineraryRequest))
        } catch (e: Exception) {
            reduce { state.copy(isLoading = false, error = e.message) }
            postSideEffect(DashboardSideEffect.ShowError(e.message ?: "Unknown error"))
        }
    }

    fun clearError() = intent { reduce { state.copy(error = null, validationErrors = emptyMap()) } }

    private fun validateInput(): Map<String, String> {
        val errors = mutableMapOf<String, String>()
        val request = container.stateFlow.value.itineraryRequest

        if (request.place.isBlank()) {
            errors["place"] = "Place is required"
        }

        if (request.days.isEmpty()) {
            errors["days"] = "At least one day is required"
        }

        return errors
    }
}
