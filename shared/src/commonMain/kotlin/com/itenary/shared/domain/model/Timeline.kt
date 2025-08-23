package com.itenary.shared.domain.model

data class Timeline(
    val timeStart: String,
    val timeEnd: String,
    val destination: String,
    val shortDescription: String,
    val longDescription: String,
    val pinpointMapsUrl: String,
    val lat: Double,
    val lng: Double,
    val expendEstimation: ExpendEstimation,
    val travelFromPrev: TravelFromPrev? = null,
    val stayDurationMin: Int,
    val stayDurationMax: Int,
    val categories: List<String>,
    val priceBand: String,
    val familyFriendly: Boolean,
    val weatherSensitivity: String,
    val source: String,
    val confidence: Double
)

data class ExpendEstimation(
    val amount: Double,
    val currency: String
)

data class TravelFromPrev(
    val mode: String,
    val durationMin: Int,
    val distanceKm: Double,
    val fareEstimate: FareEstimate? = null,
    val notes: String
)

data class FareEstimate(
    val amount: Double,
    val currency: String
)
