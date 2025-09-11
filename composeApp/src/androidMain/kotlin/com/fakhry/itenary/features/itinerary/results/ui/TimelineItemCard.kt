package com.fakhry.itenary.features.itinerary.results.ui

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.fakhry.itenary.theme.Spacing
import com.itenary.shared.domain.model.ExpendEstimation
import com.itenary.shared.domain.model.FareEstimate
import com.itenary.shared.domain.model.Timeline
import com.itenary.shared.domain.model.TravelFromPrev

@Composable
fun TimelineSection(items: MutableList<Timeline>) {
    items.forEach { timeline -> TimelineCard(timeline) }
    Spacer(modifier = Modifier.height(Spacing.xs))
}

@Composable
fun TimelineCard(item: Timeline) {
}

@Composable
@Preview
fun TimelineItemCardPreview() {
    TimelineSection(
        mutableListOf(
            // Day 1 - Visit Monas
            Timeline(
                timeStart = "09:00",
                timeEnd = "11:00",
                destination = "Visit Monas",
                shortDescription = "Explore the National Monument of Indonesia.",
                longDescription =
                    "The National Monument (Monas) is a 132m tower in the centre of Merdeka Square, Central Jakarta, symbolizing the fight for Indonesia's independence.",
                pinpointMapsUrl = "https://www.google.com/maps/place/Monas",
                lat = -6.1754,
                lng = 106.8272,
                expendEstimation = ExpendEstimation(amount = 0.0, currency = "IDR"),
                travelFromPrev =
                    TravelFromPrev(
                        mode = "Taxi",
                        durationMin = 20,
                        distanceKm = 10.0,
                        fareEstimate =
                            FareEstimate(
                                amount = 100000.0,
                                currency = "IDR"
                            ),
                        notes = "Take a taxi from your hotel to Monas"
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
                longDescription =
                    "Cafe Batavia is a historic restaurant in Jakarta's old town, known for its colonial architecture and authentic Indonesian dishes.",
                pinpointMapsUrl = "https://www.google.com/maps/place/Cafe+Batavia",
                lat = -6.1374,
                lng = 106.8281,
                expendEstimation =
                    ExpendEstimation(amount = 200000.0, currency = "IDR"),
                travelFromPrev =
                    TravelFromPrev(
                        mode = "Walk",
                        durationMin = 15,
                        distanceKm = 1.0,
                        fareEstimate =
                            FareEstimate(amount = 0.0, currency = "IDR"),
                        notes = "Short walk from Monas to Cafe Batavia"
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
                longDescription =
                    "Kota Tua (Old Town) is a historic area in Jakarta featuring Dutch colonial architecture, museums, and cultural sites.",
                pinpointMapsUrl = "https://www.google.com/maps/place/Kota+Tua",
                lat = -6.1374,
                lng = 106.8281,
                expendEstimation = ExpendEstimation(amount = 0.0, currency = "IDR"),
                travelFromPrev =
                    TravelFromPrev(
                        mode = "Taxi",
                        durationMin = 25,
                        distanceKm = 12.0,
                        fareEstimate =
                            FareEstimate(
                                amount = 120000.0,
                                currency = "IDR"
                            ),
                        notes =
                            "Take a taxi to explore different parts of Kota Tua"
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
                shortDescription =
                    "Explore one of the largest mosques in Southeast Asia.",
                longDescription =
                    "Istiqlal Mosque is the largest mosque in Southeast Asia and the third largest Sunni mosque in terms of capacity.",
                pinpointMapsUrl = "https://www.google.com/maps/place/Istiqlal+Mosque",
                lat = -6.1754,
                lng = 106.8272,
                expendEstimation = ExpendEstimation(amount = 0.0, currency = "IDR"),
                travelFromPrev =
                    TravelFromPrev(
                        mode = "Walk",
                        durationMin = 10,
                        distanceKm = 0.5,
                        fareEstimate =
                            FareEstimate(amount = 0.0, currency = "IDR"),
                        notes = "Short walk from the hotel to the mosque"
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
                longDescription =
                    "Taman Mini Indonesia Indah (Beautiful Indonesia Miniature Park) showcases the cultural diversity of Indonesia's provinces.",
                pinpointMapsUrl =
                    "https://www.google.com/maps/place/Taman+Mini+Indonesia+Indah",
                lat = -6.3000,
                lng = 106.8667,
                expendEstimation =
                    ExpendEstimation(amount = 200000.0, currency = "IDR"),
                travelFromPrev =
                    TravelFromPrev(
                        mode = "Taxi",
                        durationMin = 45,
                        distanceKm = 20.0,
                        fareEstimate =
                            FareEstimate(
                                amount = 200000.0,
                                currency = "IDR"
                            ),
                        notes = "Take a taxi to Taman Mini Indonesia Indah"
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
