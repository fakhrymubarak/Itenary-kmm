package com.fakhry.itenary.features.itinerary.results.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.fakhry.itenary.features.itinerary.results.model.StreamingDayUiModel
import com.fakhry.itenary.features.itinerary.results.model.StreamingItineraryUiModel
import com.fakhry.itenary.theme.Spacing

@Composable
fun StreamingDaysSection(streamingItinerary: StreamingItineraryUiModel) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(Spacing.md)
    ) {
        items(streamingItinerary.days) { day ->
            DayCard(day = day)
        }
    }
}

@Composable
private fun DayCard(day: StreamingDayUiModel) {
    Column(modifier = Modifier.padding(Spacing.md)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = day.dayLabel,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            if (day.isComplete) {
                Text(
                    text = "Complete",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        Spacer(modifier = Modifier.height(Spacing.sm))

        TimelineSection(items = day.items)
    }
}

