package com.fakhry.itenary.features.itinerary.results.model

data class StreamingDayUiModel(
    val dayIndex: Int = 0,
    val dayLabel: String = "",
    val items: List<TimelineItemUiModel> = emptyList(),
    val isComplete: Boolean = false,
)

data class StreamingItineraryUiModel(
    val days: List<StreamingDayUiModel> = emptyList(),
    val isComplete: Boolean = false,
)

