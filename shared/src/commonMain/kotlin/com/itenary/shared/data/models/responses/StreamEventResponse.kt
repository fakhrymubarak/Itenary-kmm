package com.itenary.shared.data.models.responses

import kotlinx.serialization.Serializable

@Serializable
data class SSEDayStartEventDto(
    val dayIndex: Int,
    val dayLabel: String,
)

@Serializable
data class SSETimelineEventDto(
    val dayIndex: Int,
    val item: TimelineItemResponse,
)

@Serializable
data class SSEDayCompleteEventDto(
    val dayIndex: Int,
    val count: Int,
)
