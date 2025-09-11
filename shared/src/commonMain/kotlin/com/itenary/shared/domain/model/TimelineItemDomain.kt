package com.itenary.shared.domain.model

data class TimelineItemDomain(
    val timeEnd: String = "",
    val timeStart: String = "",
    val lng: Float = 0f,
    val lat: Float = 0f,
    val expenseEstimation: PriceDomain = PriceDomain(),
    val pinpointMapsUrl: String = "",
    val name: String = "",
    val shortDesc: String = "",
    val category: String = "",
    val travelToNextDomain: TravelToNextDomain = TravelToNextDomain(),
    val source: String = "",
    val confidence: Int = 0,
)

data class PriceDomain(
    val amount: Int = 0,
    val currency: String = "",
)

data class TravelToNextDomain(
    val mode: String = "",
    val fareEstimate: PriceDomain = PriceDomain(),
    val distanceKm: Int = 0,
    val durationMin: Int = 0,
)
