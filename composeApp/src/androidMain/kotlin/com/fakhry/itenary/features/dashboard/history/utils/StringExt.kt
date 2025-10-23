package com.fakhry.itenary.features.dashboard.history.utils

internal fun String.formatKeyToTitle() = split("_").filter { it.isNotBlank() }.joinToString(" ") { part ->
    part.replaceFirstChar { char ->
        if (char.isLowerCase()) char.titlecase() else char.toString()
    }
}.ifBlank { "Saved Itinerary" }

