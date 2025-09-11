package com.itenary.shared.data.models.responses

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
    val travelToNext: TravelToNextResponse? = null,
    val source: String? = null,
    val confidence: Int? = null,
)

data class PriceResponse(
    val amount: Int? = null,
    val currency: String? = null,
)

data class TravelToNextResponse(
    val mode: String? = null,
    val fareEstimate: PriceResponse? = null,
    val distanceKm: Int? = null,
    val durationMin: Int? = null,
)

