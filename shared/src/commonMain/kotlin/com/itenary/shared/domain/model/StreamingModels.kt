package com.itenary.shared.domain.model

sealed class StreamingEvent {
    object Init : StreamingEvent()
    data class DayStart(val dayIndex: Int, val dayLabel: String) : StreamingEvent()
    data class TimelineItemAdded(val dayIndex: Int, val item: TimelineItemDomain) : StreamingEvent()
    data class DayComplete(val dayIndex: Int, val count: Int) : StreamingEvent()
    object Complete : StreamingEvent()
    data class Error(val message: String) : StreamingEvent()
}
