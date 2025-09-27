package com.fakhry.itenary.features.dashboard.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.fakhry.itenary.theme.Spacing

private fun tabOptions(index: UShort, title: String): TabOptions = TabOptions(index = index, title = title, icon = null)

object ItineraryTab : Tab {
    override val options: TabOptions
        @Composable get() = tabOptions(0u, "Itinerary")

    @Composable
    override fun Content() {
        PlaceholderContent(title = "Itinerary", description = "Shared itinerary request goes here")
    }
}

object FoodTab : Tab {
    override val options: TabOptions
        @Composable get() = tabOptions(1u, "Food")

    @Composable
    override fun Content() {
        PlaceholderContent(title = "Food & Restaurants", description = "Curated recommendations coming soon")
    }
}

object HistoryTab : Tab {
    override val options: TabOptions
        @Composable get() = tabOptions(2u, "History")

    @Composable
    override fun Content() {
        PlaceholderContent(title = "History", description = "Saved itineraries will appear here")
    }
}

@Composable
private fun PlaceholderContent(title: String, description: String) {
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
            Text(text = title, style = MaterialTheme.typography.headlineMedium)
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
