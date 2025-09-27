package com.fakhry.itenary.features.itinerary.results.model

import com.itenary.shared.domain.model.TimelineItemDomain
import com.itenary.shared.domain.model.TravelFromPrevDomain
import com.itenary.shared.domain.model.TravelMode
import com.itenary.shared.util.localizePrice

data class TimelineItemUiModel(
    val timeEnd: String = "",
    val timeStart: String = "",
    val lng: Float = 0f,
    val lat: Float = 0f,
    val expenseEstimation: String = "",
    val pinpointMapsUrl: String = "",
    val name: String = "",
    val shortDesc: String = "",
    val category: String = "",
    val travelFromPrev: TravelFromPrevUiModel = TravelFromPrevUiModel(),
    val source: String = "",
    val confidence: Int = 0,
)

data class TravelFromPrevUiModel(
    val mode: TravelMode = TravelMode.DEFAULT,
    val travelToNextInfo: String = "",
)

fun TimelineItemDomain.toUiModel() =
    TimelineItemUiModel(
        timeEnd = timeEnd,
        timeStart = timeStart,
        lng = lng,
        lat = lat,
        expenseEstimation =
            localizePrice(expenseEstimation.amount, expenseEstimation.currency),
        pinpointMapsUrl = pinpointMapsUrl,
        name = name,
        shortDesc = shortDesc,
        category = category,
        travelFromPrev = travelFromPrev.toTravelUiModel(),
        source = source,
        confidence = confidence,
    )

fun TravelFromPrevDomain.toTravelUiModel() =
    TravelFromPrevUiModel(
        mode = mode,
        travelToNextInfo = "$durationMin mins ${modeStr.lowercase()} $distanceKm km. ${
            localizePrice(
                fareEstimate.amount,
                fareEstimate.currency
            )
        }",
    )

