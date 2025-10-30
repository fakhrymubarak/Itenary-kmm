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
import androidx.compose.material.icons.automirrored.outlined.DirectionsWalk
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.outlined.LocalTaxi
import androidx.compose.material.icons.outlined.PedalBike
import androidx.compose.material.icons.rounded.CarCrash
import androidx.compose.material.icons.rounded.ElectricBike
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
import com.fakhry.itenary.features.itinerary.results.model.TimelineItemUiModel
import com.fakhry.itenary.features.itinerary.results.model.TravelFromPrevUiModel
import com.fakhry.itenary.features.itinerary.results.model.toUiModel
import com.fakhry.itenary.theme.ItenaryTheme
import com.fakhry.itenary.theme.Spacing
import com.itenary.shared.domain.model.PriceDomain
import com.itenary.shared.domain.model.TimelineItemDomain
import com.itenary.shared.domain.model.TravelFromPrevDomain
import com.itenary.shared.domain.model.TravelMode

@Composable
fun TimelineSection(items: MutableList<TimelineItemUiModel>) {
    Column(
        modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(Spacing.md)
    ) {
        items.forEachIndexed { index, timeline ->
            if (timeline.travelFromPrev.mode != TravelMode.DEFAULT) {
                TravelInfoCard(travel = timeline.travelFromPrev)
            }
            TimelineCard(item = timeline)
        }
    }
}

@Composable
fun TimelineCard(item: TimelineItemUiModel) {
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
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
                        text = item.name,
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
                        text = item.expenseEstimation,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                Spacer(modifier = Modifier.height(Spacing.xs))

                // Description
                Text(
                    text = item.shortDesc,
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
                        progress = { item.confidence.toFloat() / 100 },
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
fun TravelInfoCard(travel: TravelFromPrevUiModel) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 36.dp), // Align with card content
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Travel mode icon
        val travelIcon = when (travel.mode) {
            TravelMode.WALK -> Icons.AutoMirrored.Outlined.DirectionsWalk
            TravelMode.TAXI -> Icons.Outlined.LocalTaxi
            TravelMode.BIKE -> Icons.Outlined.PedalBike
            TravelMode.CAR -> Icons.Rounded.CarCrash
            TravelMode.RIDE_HAILING -> Icons.Rounded.ElectricBike
            TravelMode.OTHERS -> Icons.AutoMirrored.Outlined.DirectionsWalk
            TravelMode.DEFAULT -> Icons.AutoMirrored.Outlined.DirectionsWalk
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
            text = travel.travelToNextInfo,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
@Preview(showBackground = true)
fun TimelineItemCardPreview() {
    ItenaryTheme {
        TimelineSection(
            mutableListOf(
                // TransitionDayState 1 - Visit Monas
                TimelineItemDomain(
                    timeStart = "09:00",
                    timeEnd = "11:00",
                    name = "Visit Monas",
                    shortDesc = "Explore the National Monument of Indonesia.",
                    pinpointMapsUrl = "https://www.google.com/maps/place/Monas",
                    lat = -6.1754f,
                    lng = 106.8272f,
                    expenseEstimation = PriceDomain(amount = 0, currency = "IDR"),
                    travelFromPrev = TravelFromPrevDomain(),
                    source = "LLM",
                    confidence = 95
                ).toUiModel(),

                // TransitionDayState 1 - Lunch at Cafe Batavia
                TimelineItemDomain(
                    timeStart = "11:30",
                    timeEnd = "13:00",
                    name = "Lunch at Cafe Batavia",
                    shortDesc = "Enjoy traditional Indonesian cuisine.",
                    pinpointMapsUrl = "https://www.google.com/maps/place/Cafe+Batavia",
                    lat = -6.1374f,
                    lng = 106.8281f,
                    expenseEstimation = PriceDomain(amount = 200000, currency = "IDR"),
                    travelFromPrev = TravelFromPrevDomain(
                        mode = TravelMode.WALK,
                        modeStr = "Walk",
                        durationMin = 15,
                        distanceKm = 1f,
                        fareEstimate = PriceDomain(
                            amount = 0, currency = "IDR"
                        )
                    ),
                    source = "LLM",
                    confidence = 90
                ).toUiModel(),

                // TransitionDayState 1 - Explore Kota Tua
                TimelineItemDomain(
                    timeStart = "13:15",
                    timeEnd = "16:00",
                    name = "Explore Kota Tua",
                    shortDesc = "Wander through the old town of Jakarta.",
                    pinpointMapsUrl = "https://www.google.com/maps/place/Kota+Tua",
                    lat = -6.1374f,
                    lng = 106.8281f,
                    expenseEstimation = PriceDomain(amount = 0, currency = "IDR"),
                    travelFromPrev = TravelFromPrevDomain(
                        mode = TravelMode.TAXI,
                        modeStr = "Taxi",
                        durationMin = 25,
                        distanceKm = 12f,
                        fareEstimate = PriceDomain(
                            amount = 120000, currency = "IDR"
                        )
                    ),
                    source = "LLM",
                    confidence = 92
                ).toUiModel(),

                // TransitionDayState 2 - Visit Istiqlal Mosque
                TimelineItemDomain(
                    timeStart = "09:00",
                    timeEnd = "10:30",
                    name = "Visit Istiqlal Mosque",
                    shortDesc = "Explore one of the largest mosques in Southeast Asia.",
                    pinpointMapsUrl = "https://www.google.com/maps/place/Istiqlal+Mosque",
                    lat = -6.1754f,
                    lng = 106.8272f,
                    expenseEstimation = PriceDomain(amount = 0, currency = "IDR"),
                    travelFromPrev = TravelFromPrevDomain(
                        mode = TravelMode.WALK,
                        modeStr = "Walk",
                        durationMin = 10,
                        distanceKm = 1f,
                        fareEstimate = PriceDomain(
                            amount = 0, currency = "IDR"
                        )
                    ),
                    source = "LLM",
                    confidence = 96
                ).toUiModel(),

                // TransitionDayState 3 - Visit Taman Mini Indonesia Indah
                TimelineItemDomain(
                    timeStart = "09:00",
                    timeEnd = "14:00",
                    name = "Visit Taman Mini Indonesia Indah",
                    shortDesc = "Explore Indonesia in miniature.",
                    pinpointMapsUrl = "https://www.google.com/maps/place/Taman+Mini+Indonesia+Indah",
                    lat = -6.3000f,
                    lng = 106.8667f,
                    expenseEstimation = PriceDomain(amount = 200000, currency = "IDR"),
                    travelFromPrev = TravelFromPrevDomain(
                        mode = TravelMode.TAXI,
                        modeStr = "Taxi",
                        durationMin = 45,
                        distanceKm = 20f,
                        fareEstimate = PriceDomain(
                            amount = 200000, currency = "IDR"
                        )
                    ),
                    source = "LLM",
                    confidence = 95
                ).toUiModel()
            )
        )
    }
}

@Composable
@Preview
fun TimelineCardPreview() {
    ItenaryTheme {
        val timeline = TimelineItemDomain(
            timeStart = "09:00",
            timeEnd = "11:00",
            name = "Visit Monas",
            shortDesc = "Explore the National Monument of Indonesia.",
            pinpointMapsUrl = "https://www.google.com/maps/place/Monas",
            lat = -6.1754f,
            lng = 106.8272f,
            expenseEstimation = PriceDomain(amount = 0, currency = "IDR"),
            travelFromPrev = TravelFromPrevDomain(
                mode = TravelMode.TAXI,
                modeStr = "Taxi",
                durationMin = 20,
                distanceKm = 10f,
                fareEstimate = PriceDomain(
                    amount = 100000, currency = "IDR"
                )
            ),
            source = "LLM",
            confidence = 95
        ).toUiModel()
        TimelineCard(timeline)
    }
}
