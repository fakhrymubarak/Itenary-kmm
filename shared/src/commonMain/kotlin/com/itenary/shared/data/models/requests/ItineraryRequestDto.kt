package com.itenary.shared.data.models.requests

import kotlinx.serialization.Serializable

@Serializable
data class ItineraryRequestDto(
    val place: String,
    val days: List<DayRequestDto>,
    val language: String,
    val llm: String,
)

@Serializable
data class DayRequestDto(
    val day: Int,
    val startTime: String,
    val endTime: String,
)