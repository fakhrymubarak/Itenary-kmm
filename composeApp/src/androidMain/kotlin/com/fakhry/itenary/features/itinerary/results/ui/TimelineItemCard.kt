package com.fakhry.itenary.features.itinerary.results.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.DirectionsWalk
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fakhry.itenary.theme.ItenaryTheme
import com.fakhry.itenary.theme.Spacing
import com.itenary.shared.domain.model.ExpendEstimation
import com.itenary.shared.domain.model.FareEstimate
import com.itenary.shared.domain.model.Timeline
import com.itenary.shared.domain.model.TravelFromPrev

@Composable
fun TimelineSection(items: MutableList<Timeline>) {
    Column(
        modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(Spacing.md)
    ) {
        items.forEachIndexed { index, timeline ->
            TimelineCard(item = timeline)

            // Add travel information between items (except for the last item)
            if (index < items.size - 1) {
                timeline.travelFromPrev?.let { travel -> TravelInfoCard(travel = travel) }
            }
        }
    }
}

@Composable
fun TimelineCard(item: Timeline) {
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.Top) {
        // Circular icon indicator on the left
        Box(
            modifier = Modifier
                .size(24.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary)
                .padding(4.dp), contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.AccountBalance,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(16.dp)
            )
        }

        Spacer(modifier = Modifier.width(Spacing.md))

        // Main card content
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Spacing.md)
            ) {
                // Title row with icon and destination name
                Row(
                    modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Place,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(Spacing.xs))
                    Text(
                        text = item.destination,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Spacer(modifier = Modifier.height(Spacing.xs))

                // Time and price row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.AccessTime,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(Spacing.xs))
                        Text(
                            text = "${item.timeStart}-${item.timeEnd}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Text(
                        text = formatPrice(item.expendEstimation),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                Spacer(modifier = Modifier.height(Spacing.xs))

                // Description
                Text(
                    text = item.longDescription,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = MaterialTheme.typography.bodyMedium.lineHeight
                )

                Spacer(modifier = Modifier.height(Spacing.md))

                // Bottom row with progress indicator and Maps button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Progress indicator (circular line)
                    CircularProgressIndicator(
                        progress = {
                            item.confidence.toFloat() / 100
                        },
                        modifier = Modifier.size(16.dp),
                        color = MaterialTheme.colorScheme.primary,
                        strokeWidth = 2.dp,
                        trackColor = ProgressIndicatorDefaults.circularIndeterminateTrackColor,
                        strokeCap = ProgressIndicatorDefaults.CircularDeterminateStrokeCap,
                    )
                    // Maps button
                    OutlinedButton(
                        onClick = { /* Handle maps click */ }, modifier = Modifier.height(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(Spacing.xs))
                        Text(text = "Maps", style = MaterialTheme.typography.labelMedium)
                    }
                }
            }
        }
    }
}

@Composable
fun TravelInfoCard(travel: TravelFromPrev) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 36.dp), // Align with card content
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Travel mode icon
        val travelIcon = when (travel.mode.lowercase()) {
            "walk" -> Icons.Default.DirectionsWalk
            else -> Icons.Default.DirectionsWalk // Default to walk icon
        }

        Icon(
            imageVector = travelIcon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(16.dp)
        )

        Spacer(modifier = Modifier.width(Spacing.xs))

        // Travel information text
        Text(
            text = "${travel.durationMin} mins ${travel.mode.lowercase()} (${
                String.format(
                    "%.1f", travel.distanceKm
                )
            } km). ${
                formatPrice(travel.fareEstimate?.let {
                    ExpendEstimation(
                        it.amount, it.currency
                    )
                } ?: ExpendEstimation(0.0, "IDR"))
            }", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

private fun formatPrice(expendEstimation: ExpendEstimation): String {
    return when {
        expendEstimation.amount == 0.0 -> "Free"
        expendEstimation.amount >= 1000 -> {
            val amountInK = expendEstimation.amount / 1000
            "$${String.format("%.0f", amountInK)}.000 ${expendEstimation.currency}"
        }

        else -> "$${String.format("%.0f", expendEstimation.amount)} ${expendEstimation.currency}"
    }
}

@Composable
@Preview(showBackground = true)
fun TimelineItemCardPreview() {
    ItenaryTheme {
        TimelineSection(
            mutableListOf(
                // Day 1 - Visit Monas
                Timeline(
                    timeStart = "09:00",
                    timeEnd = "11:00",
                    destination = "Visit Monas",
                    shortDescription = "Explore the National Monument of Indonesia.",
                    longDescription = "The National Monument (Monas) is a 132m tower in the centre of Merdeka Square, Central Jakarta, symbolizing the fight for Indonesia's independence.",
                    pinpointMapsUrl = "https://www.google.com/maps/place/Monas",
                    lat = -6.1754,
                    lng = 106.8272,
                    expendEstimation = ExpendEstimation(amount = 0.0, currency = "IDR"),
                    travelFromPrev = TravelFromPrev(
                        mode = "Taxi", durationMin = 20, distanceKm = 10.0, fareEstimate = FareEstimate(
                            amount = 100000.0, currency = "IDR"
                        ), notes = "Take a taxi from your hotel to Monas"
                    ),
                    stayDurationMin = 120,
                    stayDurationMax = 120,
                    categories = listOf("Landmark", "Historical Site"),
                    priceBand = "Free",
                    familyFriendly = true,
                    weatherSensitivity = "Low",
                    source = "LLM",
                    confidence = 95.0
                ),

                // Day 1 - Lunch at Cafe Batavia
                Timeline(
                    timeStart = "11:30",
                    timeEnd = "13:00",
                    destination = "Lunch at Cafe Batavia",
                    shortDescription = "Enjoy traditional Indonesian cuisine.",
                    longDescription = "Cafe Batavia is a historic restaurant in Jakarta's old town, known for its colonial architecture and authentic Indonesian dishes.",
                    pinpointMapsUrl = "https://www.google.com/maps/place/Cafe+Batavia",
                    lat = -6.1374,
                    lng = 106.8281,
                    expendEstimation = ExpendEstimation(amount = 200000.0, currency = "IDR"),
                    travelFromPrev = TravelFromPrev(
                        mode = "Walk", durationMin = 15, distanceKm = 1.0, fareEstimate = FareEstimate(
                            amount = 0.0, currency = "IDR"
                        ), notes = "Short walk from Monas to Cafe Batavia"
                    ),
                    stayDurationMin = 90,
                    stayDurationMax = 90,
                    categories = listOf("Restaurant", "Indonesian Cuisine"),
                    priceBand = "Medium",
                    familyFriendly = true,
                    weatherSensitivity = "Low",
                    source = "LLM",
                    confidence = 90.0
                ),

                // Day 1 - Explore Kota Tua
                Timeline(
                    timeStart = "13:15",
                    timeEnd = "16:00",
                    destination = "Explore Kota Tua",
                    shortDescription = "Wander through the old town of Jakarta.",
                    longDescription = "Kota Tua (Old Town) is a historic area in Jakarta featuring Dutch colonial architecture, museums, and cultural sites.",
                    pinpointMapsUrl = "https://www.google.com/maps/place/Kota+Tua",
                    lat = -6.1374,
                    lng = 106.8281,
                    expendEstimation = ExpendEstimation(amount = 0.0, currency = "IDR"),
                    travelFromPrev = TravelFromPrev(
                        mode = "Taxi", durationMin = 25, distanceKm = 12.0, fareEstimate = FareEstimate(
                            amount = 120000.0, currency = "IDR"
                        ), notes = "Take a taxi to explore different parts of Kota Tua"
                    ),
                    stayDurationMin = 165,
                    stayDurationMax = 165,
                    categories = listOf("Historical Site", "Cultural", "Walking Tour"),
                    priceBand = "Free",
                    familyFriendly = true,
                    weatherSensitivity = "Medium",
                    source = "LLM",
                    confidence = 92.0
                ),

                // Day 2 - Visit Istiqlal Mosque
                Timeline(
                    timeStart = "09:00",
                    timeEnd = "10:30",
                    destination = "Visit Istiqlal Mosque",
                    shortDescription = "Explore one of the largest mosques in Southeast Asia.",
                    longDescription = "Istiqlal Mosque is the largest mosque in Southeast Asia and the third largest Sunni mosque in terms of capacity.",
                    pinpointMapsUrl = "https://www.google.com/maps/place/Istiqlal+Mosque",
                    lat = -6.1754,
                    lng = 106.8272,
                    expendEstimation = ExpendEstimation(amount = 0.0, currency = "IDR"),
                    travelFromPrev = TravelFromPrev(
                        mode = "Walk", durationMin = 10, distanceKm = 0.5, fareEstimate = FareEstimate(
                            amount = 0.0, currency = "IDR"
                        ), notes = "Short walk from the hotel to the mosque"
                    ),
                    stayDurationMin = 90,
                    stayDurationMax = 90,
                    categories = listOf("Religious Site", "Architecture"),
                    priceBand = "Free",
                    familyFriendly = true,
                    weatherSensitivity = "Low",
                    source = "LLM",
                    confidence = 96.0
                ),

                // Day 3 - Visit Taman Mini Indonesia Indah
                Timeline(
                    timeStart = "09:00",
                    timeEnd = "14:00",
                    destination = "Visit Taman Mini Indonesia Indah",
                    shortDescription = "Explore Indonesia in miniature.",
                    longDescription = "Taman Mini Indonesia Indah (Beautiful Indonesia Miniature Park) showcases the cultural diversity of Indonesia's provinces.",
                    pinpointMapsUrl = "https://www.google.com/maps/place/Taman+Mini+Indonesia+Indah",
                    lat = -6.3000,
                    lng = 106.8667,
                    expendEstimation = ExpendEstimation(amount = 200000.0, currency = "IDR"),
                    travelFromPrev = TravelFromPrev(
                        mode = "Taxi", durationMin = 45, distanceKm = 20.0, fareEstimate = FareEstimate(
                            amount = 200000.0, currency = "IDR"
                        ), notes = "Take a taxi to Taman Mini Indonesia Indah"
                    ),
                    stayDurationMin = 300,
                    stayDurationMax = 300,
                    categories = listOf("Theme Park", "Cultural", "Family"),
                    priceBand = "Medium",
                    familyFriendly = true,
                    weatherSensitivity = "High",
                    source = "LLM",
                    confidence = 95.0
                )
            )
        )
    }
}

@Composable
@Preview
fun TimelineCardPreview() {
    ItenaryTheme {
        val timeline = Timeline(
            timeStart = "09:00",
            timeEnd = "11:00",
            destination = "Visit Monas",
            shortDescription = "Explore the National Monument of Indonesia.",
            longDescription = "The National Monument (Monas) is a 132m tower in the centre of Merdeka Square, Central Jakarta, symbolizing the fight for Indonesia's independence.",
            pinpointMapsUrl = "https://www.google.com/maps/place/Monas",
            lat = -6.1754,
            lng = 106.8272,
            expendEstimation = ExpendEstimation(amount = 0.0, currency = "IDR"),
            travelFromPrev = TravelFromPrev(
                mode = "Taxi", durationMin = 20, distanceKm = 10.0, fareEstimate = FareEstimate(
                    amount = 100000.0, currency = "IDR"
                ), notes = "Take a taxi from your hotel to Monas"
            ),
            stayDurationMin = 120,
            stayDurationMax = 120,
            categories = listOf("Landmark", "Historical Site"),
            priceBand = "Free",
            familyFriendly = true,
            weatherSensitivity = "Low",
            source = "LLM",
            confidence = 95.0
        )
        TimelineCard(timeline)
    }

}
