package com.itenary.shared.data.mappers

import com.itenary.shared.data.models.requests.DayRequestDto
import com.itenary.shared.data.models.requests.ItineraryRequestDto
import com.itenary.shared.data.models.responses.PriceResponse
import com.itenary.shared.data.models.responses.TimelineItemResponse
import com.itenary.shared.data.models.responses.TravelFromPrev
import com.itenary.shared.domain.model.DayRequest
import com.itenary.shared.domain.model.ItineraryRequest
import com.itenary.shared.domain.model.PriceDomain
import com.itenary.shared.domain.model.TimelineItemDomain
import com.itenary.shared.domain.model.TravelFromPrevDomain
import com.itenary.shared.domain.model.TravelMode
import com.itenary.shared.util.orZero

fun ItineraryRequest.toDto(): ItineraryRequestDto = ItineraryRequestDto(
    place = place,
    days = days.map { it.toDto() },
    language = language,
    llm = llm,
)

fun DayRequest.toDto() = DayRequestDto(
    day = day,
    startTime = startTime,
    endTime = endTime,
    pace = pace,
)

fun TimelineItemResponse.toDomain(): TimelineItemDomain = TimelineItemDomain(
    timeStart = timeStart.orEmpty(),
    timeEnd = timeEnd.orEmpty(),
    lng = lng.orZero(),
    lat = lat.orZero(),
    expenseEstimation = expenseEstimation?.toDomain() ?: PriceDomain(),
    pinpointMapsUrl = pinpointMapsUrl.orEmpty(),
    name = name.orEmpty(),
    shortDesc = shortDesc.orEmpty(),
    category = category.orEmpty(),
    travelFromPrev = travelFromPrev?.toDomain() ?: TravelFromPrevDomain(),
    source = source.orEmpty(),
    confidence = confidence.orZero()
)

fun PriceResponse.toDomain(): PriceDomain = PriceDomain(amount = amount.orZero(), currency = currency.orEmpty())

fun TravelFromPrev.toDomain(): TravelFromPrevDomain = TravelFromPrevDomain(
    mode = mode.toTravelModeDomain(),
    modeStr = mode.orEmpty(),
    fareEstimate = fareEstimate?.toDomain() ?: PriceDomain(),
    distanceKm = distanceKm.orZero(),
    durationMin = durationMin.orZero()
)

private fun String?.toTravelModeDomain(): TravelMode {
    return when (this?.lowercase()) {
        "walk" -> TravelMode.WALK
        "taxi" -> TravelMode.TAXI
        "bike" -> TravelMode.BIKE
        "car" -> TravelMode.CAR
        "ride hailing" -> TravelMode.RIDE_HAILING
        else -> TravelMode.OTHERS
    }
}
