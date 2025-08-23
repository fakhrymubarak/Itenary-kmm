package com.itenary.shared.domain.model

sealed class StreamingEvent {
    data class Init(val request: ItineraryRequest) : StreamingEvent()
    data class DayStart(val dayIndex: Int, val dayLabel: String) : StreamingEvent()
    data class TimelineItemAdded(val dayIndex: Int, val item: Timeline) : StreamingEvent()
    data class DayComplete(val dayIndex: Int, val count: Int) : StreamingEvent()
    object Complete : StreamingEvent()
    data class Error(val message: String) : StreamingEvent()
}

data class StreamingItinerary(
    val days: MutableList<StreamingDay> = mutableListOf(),
    val isComplete: Boolean = false
)

data class StreamingDay(
    val dayIndex: Int,
    val dayLabel: String,
    val items: MutableList<Timeline> = mutableListOf(),
    val isComplete: Boolean = false
)
