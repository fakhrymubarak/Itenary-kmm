package com.itenary.shared.domain.model

data class ItineraryRequest(
    val place: String,
    val days: List<DayRequest>,
    val language: String,
    val llm: String,
)

data class DayRequest(
    val day: Int,
    val startTime: String,
    val endTime: String,
    val pace: Int,
)
