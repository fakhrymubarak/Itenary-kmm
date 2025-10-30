package com.itenary.shared.data.models.responses

import kotlinx.serialization.Serializable

@Serializable
data class TimelineItemResponse(
    val timeEnd: String? = null,
    val timeStart: String? = null,
    val lng: Float? = null,
    val lat: Float? = null,
    val expenseEstimation: PriceResponse? = null,
    val pinpointMapsUrl: String? = null,
    val name: String? = null,
    val shortDesc: String? = null,
    val category: String? = null,
    val travelFromPrev: TravelFromPrev? = null,
    val source: String? = null,
    val confidence: Int? = null,
)

@Serializable
data class TravelFromPrev(
    val mode: String? = null,
    val fareEstimate: PriceResponse? = null,
    val distanceKm: Float? = null,
    val durationMin: Int? = null,
)

@Serializable
data class PriceResponse(
    val amount: Int? = null,
    val currency: String? = null,
)
