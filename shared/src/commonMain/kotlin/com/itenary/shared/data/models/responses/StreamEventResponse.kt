package com.itenary.shared.data.models.responses

import com.itenary.shared.data.models.requests.ItineraryRequestDto
import kotlinx.serialization.Serializable

@Serializable
data class SSEEventDto(
    val event: String,
    val data: String
)

@Serializable
data class SSEInitEventDto(
    val request: ItineraryRequestDto
)

@Serializable
data class SSEDayStartEventDto(
    val dayIndex: Int,
    val dayLabel: String
)

@Serializable
data class SSETimelineEventDto(
    val dayIndex: Int,
    val item: TimelineItemResponse
)

@Serializable
data class SSEDayCompleteEventDto(
    val dayIndex: Int,
    val count: Int
)

@Serializable
data class SSECompleteEventDto(
    val data: String = ""
)