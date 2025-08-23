package com.itenary.shared.domain.model

data class Itinerary(
    val result: List<Day>
)

data class Day(
    val day: String,
    val timeline: List<TimelineItem>
)

data class TimelineItem(
    val timeStart: String,
    val timeEnd: String,
    val destination: String,
    val shortDescription: String,
    val longDescription: String,
    val pinpointMapsUrl: String,
    val lat: Double,
    val lng: Double,
    val expendEstimation: ExpendEstimation,
    val travelFromPrev: TravelFromPrev?,
    val stayDurationMin: Int,
    val stayDurationMax: Int,
    val categories: List<String>,
    val priceBand: PriceBand,
    val familyFriendly: Boolean,
    val weatherSensitivity: WeatherSensitivity,
    val source: String,
    val confidence: Double
)

data class ExpendEstimation(
    val amount: Double,
    val currency: String
)

data class TravelFromPrev(
    val mode: TransportMode,
    val durationMin: Int,
    val distanceKm: Double,
    val fareEstimate: FareEstimate?,
    val notes: String
)

data class FareEstimate(
    val amount: Double,
    val currency: String
)

sealed class TransportMode {
    data object Walk : TransportMode()
    data object Drive : TransportMode()
    data object RideHail : TransportMode()
    data object MRT : TransportMode()
    data object LRT : TransportMode()
    data object Bus : TransportMode()
    data class Unknown(val raw: String) : TransportMode()
}

sealed class WeatherSensitivity {
    data object Indoor : WeatherSensitivity()
    data object Outdoor : WeatherSensitivity()
    data object RainSafe : WeatherSensitivity()
    data class Unknown(val raw: String) : WeatherSensitivity()
}

sealed class PriceBand {
    data object One : PriceBand() // $
    data object Two : PriceBand() // $$
    data object Three : PriceBand() // $$$
    data class Unknown(val raw: String) : PriceBand()
}

data class ItineraryRequest(
    val city: String,
    val province: String,
    val countryCode: String,
    val startDate: String,
    val endDate: String,
    val language: String
)


