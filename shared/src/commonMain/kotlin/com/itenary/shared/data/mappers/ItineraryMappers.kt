package com.itenary.shared.data.mappers

import com.itenary.shared.data.models.requests.DayRequestDto
import com.itenary.shared.data.models.requests.ItineraryRequestDto
import com.itenary.shared.data.models.responses.MoneyResponse
import com.itenary.shared.data.models.responses.TimelineItemResponse
import com.itenary.shared.data.models.responses.TravelFromPrevResponse
import com.itenary.shared.domain.model.DayRequest
import com.itenary.shared.domain.model.ExpendEstimation
import com.itenary.shared.domain.model.FareEstimate
import com.itenary.shared.domain.model.ItineraryRequest
import com.itenary.shared.domain.model.Timeline
import com.itenary.shared.domain.model.TravelFromPrev

fun ItineraryRequest.toDto(): ItineraryRequestDto =
        ItineraryRequestDto(
                place = place,
                days = days.map { it.toDto() },
                language = language,
                llm = llm
        )

fun DayRequest.toDto(): DayRequestDto =
        DayRequestDto(day = day, startTime = startTime, endTime = endTime)

fun ItineraryRequestDto.toDomain(): ItineraryRequest =
        ItineraryRequest(
                place = place,
                days = days.map { it.toDomain() },
                language = language,
                llm = llm
        )

fun DayRequestDto.toDomain(): DayRequest =
        DayRequest(day = day, startTime = startTime, endTime = endTime)

fun TimelineItemResponse.toDomain(): Timeline =
        Timeline(
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
                priceBand = priceBand,
                familyFriendly = familyFriendly,
                weatherSensitivity = weatherSensitivity,
                source = source,
                confidence = confidence
        )

fun MoneyResponse.toDomain(): ExpendEstimation =
        ExpendEstimation(amount = amount, currency = currency)

fun TravelFromPrevResponse.toDomain(): TravelFromPrev =
        TravelFromPrev(
                mode = mode,
                durationMin = durationMin,
                distanceKm = distanceKm,
                fareEstimate = fareEstimate?.toFareEstimate(),
                notes = notes
        )

fun MoneyResponse.toFareEstimate(): FareEstimate =
        FareEstimate(amount = amount, currency = currency)
