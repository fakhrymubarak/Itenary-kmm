package com.fakhry.itenary.model

import com.itenary.shared.domain.model.DayRequest
import com.itenary.shared.domain.model.ItineraryRequest
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
    val id: Int = 0,
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

fun ItineraryRequestUiModel.toDomain() = ItineraryRequest(
    place = place,
    days = days.toListDayRequest(),
    language = language,
    llm = llm,
)
