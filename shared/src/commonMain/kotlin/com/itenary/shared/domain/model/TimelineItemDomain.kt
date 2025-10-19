package com.itenary.shared.domain.model

data class ItineraryDomain(
    val days: MutableList<DayDomain> = mutableListOf(),
)

data class DayDomain(
    val dayIndex: Int,
    val dayLabel: String,
    val items: MutableList<TimelineItemDomain> = mutableListOf(),
)

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
    val travelFromPrev: TravelFromPrevDomain = TravelFromPrevDomain(),
    val source: String = "",
    val confidence: Int = 0,
)

data class PriceDomain(
    val amount: Int = 0,
    val currency: String = "",
)

data class TravelFromPrevDomain(
    val mode: TravelMode = TravelMode.DEFAULT,
    val modeStr: String = "",
    val fareEstimate: PriceDomain = PriceDomain(),
    val distanceKm: Float = 0f,
    val durationMin: Int = 0,
)

enum class TravelMode {
    WALK, TAXI, BIKE, CAR, RIDE_HAILING, OTHERS, DEFAULT
}
