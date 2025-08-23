package com.fakhry.itenary.commons.model

import com.itenary.shared.domain.model.DayRequest
import kotlinx.serialization.Serializable

@Serializable
data class ItineraryRequestUiModel(
    val place: String = "",
    val days: List<DayRequestUiModel> = emptyList(),
    val language: String = "",
    val llm: String = "",
)

@Serializable
data class DayRequestUiModel(
    val day: Int = 0,
    val startTime: String = "",
    val endTime: String = "",
)

fun List<DayRequestUiModel>.toListDayRequest() = map {
    DayRequest(
        day = it.day,
        startTime = it.startTime,
        endTime = it.endTime
    )
}
