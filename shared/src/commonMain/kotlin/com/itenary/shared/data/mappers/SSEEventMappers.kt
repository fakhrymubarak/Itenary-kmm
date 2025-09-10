package com.itenary.shared.data.mappers

import com.itenary.shared.data.models.responses.SSEDayCompleteEventDto
import com.itenary.shared.data.models.responses.SSEDayStartEventDto
import com.itenary.shared.data.models.responses.SSEEventDto
import com.itenary.shared.data.models.responses.SSETimelineEventDto
import com.itenary.shared.domain.model.SSEEventType
import com.itenary.shared.domain.model.StreamingEvent
import com.itenary.shared.domain.model.toSSEEventType
import kotlinx.serialization.json.Json

class SSEEventMapper {
    private val json = Json {
        ignoreUnknownKeys = true
        explicitNulls = false
    }

    fun mapToStreamingEvent(sseEvent: SSEEventDto): StreamingEvent {
        val eventType = sseEvent.event.toSSEEventType()

        return when (eventType) {
            SSEEventType.INIT -> StreamingEvent.Init

            SSEEventType.DAY_START -> {
                val dayStartEvent = json.decodeFromString<SSEDayStartEventDto>(sseEvent.data)
                StreamingEvent.DayStart(dayStartEvent.dayIndex, dayStartEvent.dayLabel)
            }

            SSEEventType.TIMELINE -> {
                val timelineEvent = json.decodeFromString<SSETimelineEventDto>(sseEvent.data)
                val timelineItem = timelineEvent.item.toDomain()
                StreamingEvent.TimelineItemAdded(timelineEvent.dayIndex, timelineItem)
            }

            SSEEventType.DAY_COMPLETE -> {
                val dayCompleteEvent = json.decodeFromString<SSEDayCompleteEventDto>(sseEvent.data)
                StreamingEvent.DayComplete(dayCompleteEvent.dayIndex, dayCompleteEvent.count)
            }

            SSEEventType.COMPLETE -> StreamingEvent.Complete

            SSEEventType.ERROR -> {
                StreamingEvent.Error("Streaming error: ${sseEvent.data}")
            }
        }
    }
}
