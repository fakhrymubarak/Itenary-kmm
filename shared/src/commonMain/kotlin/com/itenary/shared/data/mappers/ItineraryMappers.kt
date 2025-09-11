package com.itenary.shared.data.mappers

import com.itenary.shared.data.models.requests.DayRequestDto
import com.itenary.shared.data.models.requests.ItineraryRequestDto
import com.itenary.shared.data.models.responses.PriceResponse
import com.itenary.shared.data.models.responses.TimelineItemResponse
import com.itenary.shared.data.models.responses.TravelToNextResponse
import com.itenary.shared.domain.model.DayRequest
import com.itenary.shared.domain.model.ItineraryRequest
import com.itenary.shared.domain.model.PriceDomain
import com.itenary.shared.domain.model.TimelineItemDomain
import com.itenary.shared.domain.model.TravelToNextDomain
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
    travelToNextDomain = travelToNext?.toDomain() ?: TravelToNextDomain(),
    source = source.orEmpty(),
    confidence = confidence.orZero()
)

fun PriceResponse.toDomain(): PriceDomain = PriceDomain(amount = amount.orZero(), currency = currency.orEmpty())

fun TravelToNextResponse.toDomain(): TravelToNextDomain = TravelToNextDomain(
    mode = mode.orEmpty(),
    fareEstimate = fareEstimate?.toDomain() ?: PriceDomain(),
    distanceKm = distanceKm.orZero(),
    durationMin = durationMin.orZero()
)
