package com.itenary.shared.data.mappers

import com.itenary.shared.data.dto.*
import com.itenary.shared.domain.model.*

fun ItineraryRequest.toDto(): ItineraryRequestDto = ItineraryRequestDto(
    city = city,
    province = province,
    countryCode = countryCode,
    startDate = startDate,
    endDate = endDate,
    language = language
)

fun ItineraryResponseDto.toDomain(): Itinerary = Itinerary(
    result = result.map { it.toDomain() }
)

fun DayDto.toDomain(): Day = Day(
    day = day,
    timeline = timeline.map { it.toDomain() }
)

fun TimelineItemDto.toDomain(): TimelineItem = TimelineItem(
    timeStart = timeStart,
    timeEnd = timeEnd,
    destination = destination,
    shortDescription = shortDescription,
    longDescription = longDescription,
    pinpointMapsUrl = pinpointMapsUrl,
    lat = lat,
    lng = lng,
    expendEstimation = expendEstimation.toDomain(),
    travelFromPrev = travelFromPrev?.toDomain(),
    stayDurationMin = stayDurationMin,
    stayDurationMax = stayDurationMax,
    categories = categories,
    priceBand = priceBand.toPriceBand(),
    familyFriendly = familyFriendly,
    weatherSensitivity = weatherSensitivity.toWeatherSensitivity(),
    source = source,
    confidence = confidence
)

fun ExpendEstimationDto.toDomain(): ExpendEstimation = ExpendEstimation(
    amount = amount,
    currency = currency
)

fun TravelFromPrevDto.toDomain(): TravelFromPrev = TravelFromPrev(
    mode = mode.toTransportMode(),
    durationMin = durationMin,
    distanceKm = distanceKm,
    fareEstimate = fareEstimate?.toDomain(),
    notes = notes
)

fun FareEstimateDto.toDomain(): FareEstimate = FareEstimate(
    amount = amount,
    currency = currency
)

private fun String.toTransportMode(): TransportMode = when (lowercase()) {
    "walk" -> TransportMode.Walk
    "drive" -> TransportMode.Drive
    "ridehail" -> TransportMode.RideHail
    "mrt" -> TransportMode.MRT
    "lrt" -> TransportMode.LRT
    "bus" -> TransportMode.Bus
    else -> TransportMode.Unknown(this)
}

private fun String.toWeatherSensitivity(): WeatherSensitivity = when (lowercase()) {
    "indoor" -> WeatherSensitivity.Indoor
    "outdoor" -> WeatherSensitivity.Outdoor
    "rain-safe", "rainsafe", "rain_safe" -> WeatherSensitivity.RainSafe
    else -> WeatherSensitivity.Unknown(this)
}

private fun String.toPriceBand(): PriceBand = when (this) {
    "$" -> PriceBand.One
    "$$" -> PriceBand.Two
    "$$$" -> PriceBand.Three
    else -> PriceBand.Unknown(this)
}


