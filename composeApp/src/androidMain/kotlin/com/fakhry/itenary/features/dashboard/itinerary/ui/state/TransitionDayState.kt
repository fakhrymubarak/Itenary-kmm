package com.fakhry.itenary.features.dashboard.itinerary.ui.state

import androidx.compose.animation.core.MutableTransitionState
import com.fakhry.itenary.commons.model.DayRequestUiModel

data class TransitionDayState(
    val day: DayRequestUiModel,
    val transitionState: MutableTransitionState<Boolean>,
)
