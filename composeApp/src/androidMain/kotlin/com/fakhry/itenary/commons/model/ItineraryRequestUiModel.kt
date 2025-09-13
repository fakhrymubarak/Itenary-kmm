package com.fakhry.itenary.commons.model

import com.itenary.shared.domain.model.DayRequest
import kotlinx.serialization.Serializable

@Serializable
data class ItineraryRequestUiModel(
    val place: String = "",
    val days: List<DayRequestUiModel> = emptyList(),
    val language: String = "English",
    val llm: String = "mock",
)

@Serializable
data class DayRequestUiModel(
    val day: Int = 1,
    val startTime: String = "09:00",
    val endTime: String = "18:00",
    val pace: Float = 6f,
)

fun List<DayRequestUiModel>.toListDayRequest() = map {
    DayRequest(
        day = it.day,
        startTime = it.startTime,
        endTime = it.endTime,
        pace = it.pace.toInt(),
    )
}
