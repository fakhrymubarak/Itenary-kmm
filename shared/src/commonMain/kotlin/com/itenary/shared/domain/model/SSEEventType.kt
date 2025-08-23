package com.itenary.shared.domain.model

enum class SSEEventType(val value: String) {
    INIT("init"),
    DAY_START("day_start"),
    TIMELINE("timeline"),
    DAY_COMPLETE("day_complete"),
    COMPLETE("complete"),
    ERROR("error")
}

fun String?.toSSEEventType(): SSEEventType {
    return when (this) {
        "init" -> SSEEventType.INIT
        "day_start" -> SSEEventType.DAY_START
        "timeline" -> SSEEventType.TIMELINE
        "day_complete" -> SSEEventType.DAY_COMPLETE
        "complete" -> SSEEventType.COMPLETE
        else -> SSEEventType.ERROR
    }
}