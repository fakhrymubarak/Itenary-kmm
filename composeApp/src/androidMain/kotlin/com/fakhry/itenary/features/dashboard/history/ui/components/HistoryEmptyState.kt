package com.fakhry.itenary.features.dashboard.history.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.fakhry.itenary.theme.ItenaryTheme
import com.fakhry.itenary.theme.Spacing


@Composable
fun HistoryEmptyState() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Spacing.md)
    ) {
        Text(
            text = "🔖",
            style = MaterialTheme.typography.displayLarge
        )
        Text(
            text = "No saved itineraries yet",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center
        )
        Text(
            text = "Generate an itinerary and save it to revisit it here.",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}


@Preview(showBackground = true)
@Composable
private fun HistoryEmptyStatePreview() {
    ItenaryTheme {
        HistoryEmptyState()
    }
}
