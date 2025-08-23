package com.itenary.shared.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class ItineraryResponseDto(
    val result: List<DayDto>
)

@Serializable
data class DayDto(
    val day: String,
    val timeline: List<TimelineItemDto>
)

@Serializable
data class TimelineItemDto(
    val timeStart: String,
    val timeEnd: String,
    val destination: String,
    val shortDescription: String,
    val longDescription: String,
    val pinpointMapsUrl: String,
    val lat: Double,
    val lng: Double,
    val expendEstimation: ExpendEstimationDto,
    val travelFromPrev: TravelFromPrevDto? = null,
    val stayDurationMin: Int,
    val stayDurationMax: Int,
    val categories: List<String>,
    val priceBand: String,
    val familyFriendly: Boolean,
    val weatherSensitivity: String,
    val source: String,
    val confidence: Double
)

@Serializable
data class ExpendEstimationDto(
    val amount: Double,
    val currency: String
)

@Serializable
data class TravelFromPrevDto(
    val mode: String,
    val durationMin: Int,
    val distanceKm: Double,
    val fareEstimate: FareEstimateDto? = null,
    val notes: String
)

@Serializable
data class FareEstimateDto(
    val amount: Double,
    val currency: String
)

@Serializable
data class ErrorEnvelopeDto(
    val error: ErrorDto? = null
)

@Serializable
data class ErrorDto(
    val code: String? = null,
    val message: String = ""
)


