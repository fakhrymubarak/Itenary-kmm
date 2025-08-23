package com.itenary.shared

import com.itenary.shared.data.dto.*
import com.itenary.shared.data.mappers.toDomain
import kotlin.test.Test
import kotlin.test.assertTrue

class MappersTest {
    @Test
    fun unknown_enums_map_to_unknown() {
        val dto = TimelineItemDto(
            timeStart = "08:00",
            timeEnd = "09:00",
            destination = "A",
            shortDescription = "s",
            longDescription = "l",
            pinpointMapsUrl = "u",
            lat = 1.0,
            lng = 2.0,
            expendEstimation = ExpendEstimationDto(1.0, "USD"),
            travelFromPrev = TravelFromPrevDto(
                mode = "hoverboard",
                durationMin = 10,
                distanceKm = 1.0,
                fareEstimate = FareEstimateDto(1.0, "USD"),
                notes = "n"
            ),
            stayDurationMin = 10,
            stayDurationMax = 20,
            categories = listOf("x"),
            priceBand = "$$$$",
            familyFriendly = true,
            weatherSensitivity = "martian",
            source = "s",
            confidence = 0.5
        )
        val domain = dto.toDomain()
        assertTrue(domain.priceBand is com.itenary.shared.domain.model.PriceBand.Unknown)
        assertTrue(domain.weatherSensitivity is com.itenary.shared.domain.model.WeatherSensitivity.Unknown)
        assertTrue(domain.travelFromPrev!!.mode is com.itenary.shared.domain.model.TransportMode.Unknown)
    }
}


