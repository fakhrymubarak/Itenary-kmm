package com.itenary.shared.data.models.responses

import kotlinx.serialization.Serializable

@Serializable
data class TimelineItemResponse(
    val timeStart: String,
    val timeEnd: String,
    val destination: String,
    val shortDescription: String,
    val longDescription: String,
    val pinpointMapsUrl: String,
    val lat: Double,
    val lng: Double,
    val expendEstimation: MoneyResponse,
    val travelFromPrev: TravelFromPrevResponse? = null,
    val stayDurationMin: Int,
    val stayDurationMax: Int,
    val categories: List<String>,
    val priceBand: String,
    val familyFriendly: Boolean,
    val weatherSensitivity: String,
    val source: String,
    val confidence: Double,
)

@Serializable
data class MoneyResponse(
    val amount: Double,
    val currency: String,
)

@Serializable
data class TravelFromPrevResponse(
    val mode: String,
    val durationMin: Int,
    val distanceKm: Double,
    val fareEstimate: MoneyResponse? = null,
    val notes: String,
)
