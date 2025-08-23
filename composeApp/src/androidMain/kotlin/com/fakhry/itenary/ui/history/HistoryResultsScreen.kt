package com.fakhry.itenary.ui.history

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.fakhry.itenary.theme.Spacing

@Composable
fun HistoryResultsScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(Spacing.lg),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Spacing.md)
        ) {
            Text(
                text = "📋",
                style = MaterialTheme.typography.displayLarge
            )
            Text(
                text = "History Results",
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center
            )
            Text(
                text = "Your itinerary history will appear here!",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
