package com.fakhry.itenary.features.itinerary.results.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.fakhry.itenary.features.itinerary.results.model.TimelineItemUiModel
import com.fakhry.itenary.features.itinerary.results.model.TravelFromPrevUiModel
import com.fakhry.itenary.theme.Spacing
import com.itenary.shared.domain.model.TravelMode

@Composable
fun TimelineSection(items: List<TimelineItemUiModel>) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(Spacing.md)
    ) {
        items.forEach { item ->
            if (item.travelFromPrev.mode != TravelMode.DEFAULT) {
                TravelInfoCard(travel = item.travelFromPrev)
            }
            TimelineCard(item = item)
        }
    }
}

@Composable
fun TimelineCard(item: TimelineItemUiModel) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(Spacing.md)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = item.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.width(Spacing.sm))

            Text(
                text = item.expenseEstimation,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(modifier = Modifier.height(Spacing.xs))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = item.category, style = MaterialTheme.typography.bodySmall)
        }

        Spacer(modifier = Modifier.height(Spacing.sm))

        Text(
            text = item.shortDesc,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun IconText(text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        // TODO: Provide multiplatform icon resource
        Spacer(modifier = Modifier.size(Spacing.xs))
        Text(text = text, style = MaterialTheme.typography.bodySmall)
    }
}

@Composable
fun TravelInfoCard(travel: TravelFromPrevUiModel) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Spacing.xs)
    ) {
        // TODO: Provide multiplatform icon resource for travel mode
        Text(
            text = travel.mode.name,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.primary
        )

        Text(
            text = travel.travelToNextInfo,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

