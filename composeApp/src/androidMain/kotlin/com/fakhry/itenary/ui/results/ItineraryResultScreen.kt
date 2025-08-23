package com.fakhry.itenary.ui.results

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.fakhry.itenary.ItineraryViewModel
import com.fakhry.itenary.navigation.NavigationViewModel
import com.fakhry.itenary.theme.Spacing
import com.itenary.shared.domain.model.StreamingDay
import com.itenary.shared.domain.model.Timeline
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItineraryResultScreen(
    onNavigateBack: () -> Unit,
    navViewModel: NavigationViewModel = koinViewModel(),
    viewModel: ItineraryViewModel = koinViewModel(),
) {
    val requestState by navViewModel.itineraryRequestState.collectAsState(null)
    val state by viewModel.streamingState.collectAsState()


    requestState?.let { viewModel.streamItineraryGeneration(it) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Itinerary Results") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                state.isLoading -> {
                    // Loading state
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator()
                        Spacer(modifier = Modifier.height(Spacing.md))
                        Text(
                            text = "Generating your itinerary...",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }

                state.streamingItinerary != null -> {
                    // Streaming results
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(Spacing.md),
                        verticalArrangement = Arrangement.spacedBy(Spacing.md)
                    ) {
                        // Show streaming days
                        val streamingItinerary = state.streamingItinerary
                        if (streamingItinerary != null) {
                            items(streamingItinerary.days) { day ->
                                DayCard(day = day)
                            }

                            // Show completion status
                            if (streamingItinerary.isComplete) {
                                item {
                                    Card(
                                        modifier = Modifier.fillMaxWidth(),
                                        colors = CardDefaults.cardColors(
                                            containerColor = MaterialTheme.colorScheme.secondaryContainer
                                        )
                                    ) {
                                        Column(
                                            modifier = Modifier.padding(Spacing.md)
                                        ) {
                                            Text(
                                                text = "✅ Itinerary Complete!",
                                                style = MaterialTheme.typography.headlineSmall,
                                                fontWeight = FontWeight.Bold
                                            )
                                            Spacer(modifier = Modifier.height(Spacing.sm))
                                            Text(
                                                text = "Your personalized itinerary is ready. You can now plan your trip!",
                                                style = MaterialTheme.typography.bodyMedium
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                state.error != null -> {
                    // Error state
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(Spacing.md),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "❌ Error",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(Spacing.md))
                        Text(
                            text = state.error ?: "Something went wrong",
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Spacer(modifier = Modifier.height(Spacing.lg))
                        Button(onClick = onNavigateBack) {
                            Text("Go Back")
                        }
                    }
                }

                else -> return@Box
            }
        }
    }
}

@Composable
private fun DayCard(
    day: StreamingDay,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(Spacing.md)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = day.dayLabel,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                if (day.isComplete) {
                    Text(
                        text = "✅ Complete",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                } else {
                    Text(
                        text = "⏳ In Progress",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(Spacing.sm))

            // Show timeline items
            day.items.forEach { item ->
                TimelineItemCard(item = item)
                Spacer(modifier = Modifier.height(Spacing.xs))
            }

            if (day.items.isEmpty() && !day.isComplete) {
                Text(
                    text = "Generating activities...",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun TimelineItemCard(item: Timeline) {
    TODO("Not yet implemented")
}