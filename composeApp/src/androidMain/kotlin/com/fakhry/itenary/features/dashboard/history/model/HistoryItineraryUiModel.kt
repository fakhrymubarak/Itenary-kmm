package com.fakhry.itenary.features.dashboard.history.model

data class HistoryItineraryUiModel(
    val key: String = "",
    val title: String = "",
    val totalDays: String = "",
    val deletionStatus: DeleteStatus = DeleteStatus.INITIAL,
)

enum class DeleteStatus {
    INITIAL, SHOW_ALERT_USER, CANCELLED
}
