package com.fakhry.itenary.features.itinerary.results.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.fakhry.itenary.StreamingDayUiModel
import com.fakhry.itenary.StreamingItineraryUiModel
import com.fakhry.itenary.features.itinerary.results.model.TimelineItemUiModel
import com.fakhry.itenary.features.itinerary.results.model.TravelFromPrevUiModel
import com.fakhry.itenary.theme.ItenaryTheme
import com.fakhry.itenary.theme.Spacing
import com.itenary.shared.domain.model.TravelMode

@Composable
fun StreamingDaysSection(streamingItinerary: StreamingItineraryUiModel) {
    // Streaming results
    LazyColumn(
        modifier = Modifier.Companion
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(Spacing.md)
    ) {
        items(streamingItinerary.days) { day ->
            DayCard(day = day)
        }

        // Show completion status
        if (streamingItinerary.isComplete) {
            item {
                Card(
                    modifier = Modifier.Companion.fillMaxWidth(),
                    colors =
                        CardDefaults.cardColors(
                            containerColor =
                                MaterialTheme.colorScheme
                                    .secondaryContainer
                        )
                ) {
                    Column(modifier = Modifier.Companion.padding(Spacing.md)) {
                        Text(
                            text = "✅ Itinerary Complete!",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Companion.Bold
                        )
                        Spacer(modifier = Modifier.Companion.height(Spacing.sm))
                        Text(
                            text =
                                "Your personalized itinerary is ready. You can now plan your trip!",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}


@Composable
private fun DayCard(
    day: StreamingDayUiModel,
) {
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
                    modifier = Modifier,
                    text = "✅ Complete",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
            } else {
                Text(
                    modifier = Modifier,
                    text = "⏳ In Progress",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }

        Spacer(modifier = Modifier.height(Spacing.sm))

        // Show timeline items
        TimelineSection(items = day.items)

        if (day.items.isEmpty() && !day.isComplete) {
            Text(
                text = "Generating activities...",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun StreamingDaysSectionPreview() {
    ItenaryTheme {
        StreamingDaysSection(
            streamingItinerary = StreamingItineraryUiModel(
                days = mutableListOf(
                    // Day 1 - Complete
                    StreamingDayUiModel(
                        dayIndex = 0,
                        dayLabel = "Day 1 - Arrival & City Exploration",
                        items = mutableListOf(
                            TimelineItemUiModel(
                                timeStart = "09:00",
                                timeEnd = "11:00",
                                name = "Tokyo Station",
                                shortDesc = "Arrive at Tokyo Station and explore the historic building",
                                category = "Transportation",
                                expenseEstimation = "¥500",
                                travelFromPrev = TravelFromPrevUiModel(
                                    mode = TravelMode.DEFAULT,
                                    travelToNextInfo = ""
                                )
                            ),
                            TimelineItemUiModel(
                                timeStart = "11:30",
                                timeEnd = "13:00",
                                name = "Imperial Palace East Gardens",
                                shortDesc = "Visit the beautiful gardens and learn about Japanese history",
                                category = "Sightseeing",
                                expenseEstimation = "¥0",
                                travelFromPrev = TravelFromPrevUiModel(
                                    mode = TravelMode.WALK,
                                    travelToNextInfo = "15 mins walking 1.2 km. ¥0"
                                )
                            ),
                            TimelineItemUiModel(
                                timeStart = "13:30",
                                timeEnd = "14:30",
                                name = "Ginza District",
                                shortDesc = "Explore luxury shopping and traditional crafts",
                                category = "Shopping",
                                expenseEstimation = "¥2000",
                                travelFromPrev = TravelFromPrevUiModel(
                                    mode = TravelMode.DEFAULT,
                                    travelToNextInfo = "8 mins subway 2.1 km. ¥170"
                                )
                            )
                        ),
                        isComplete = true
                    ),
                    // Day 2 - In Progress
                    StreamingDayUiModel(
                        dayIndex = 1,
                        dayLabel = "Day 2 - Traditional Culture & Temples",
                        items = mutableListOf(
                            TimelineItemUiModel(
                                timeStart = "08:00",
                                timeEnd = "10:00",
                                name = "Senso-ji Temple",
                                shortDesc = "Tokyo's oldest temple in Asakusa district",
                                category = "Temple",
                                expenseEstimation = "¥0",
                                travelFromPrev = TravelFromPrevUiModel(
                                    mode = TravelMode.DEFAULT,
                                    travelToNextInfo = ""
                                )
                            ),
                            TimelineItemUiModel(
                                timeStart = "10:30",
                                timeEnd = "12:00",
                                name = "Nakamise Shopping Street",
                                shortDesc = "Traditional shopping street with local souvenirs",
                                category = "Shopping",
                                expenseEstimation = "¥1500",
                                travelFromPrev = TravelFromPrevUiModel(
                                    mode = TravelMode.WALK,
                                    travelToNextInfo = "5 mins walking 0.3 km. ¥0"
                                )
                            )
                        ),
                        isComplete = false
                    ),
                    // Day 3 - Empty (generating)
                    StreamingDayUiModel(
                        dayIndex = 2,
                        dayLabel = "Day 3 - Modern Tokyo & Entertainment",
                        items = mutableListOf(),
                        isComplete = false
                    )
                ),
                isComplete = false
            )
        )
    }
}
