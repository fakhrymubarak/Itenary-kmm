package com.fakhry.itenary.features.itinerary.results.model

data class StreamingItineraryUiModel(
    val days: MutableList<StreamingDayUiModel> = mutableListOf(),
    val isComplete: Boolean = false,
)

data class StreamingDayUiModel(
    val dayIndex: Int,
    val dayLabel: String,
    val items: MutableList<TimelineItemUiModel> = mutableListOf(),
    val isComplete: Boolean = false,
)
