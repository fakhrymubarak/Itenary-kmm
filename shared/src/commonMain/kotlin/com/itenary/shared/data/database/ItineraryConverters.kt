package com.itenary.shared.data.database

import androidx.room.TypeConverter
import com.itenary.shared.domain.model.DayDomain
import com.itenary.shared.domain.model.ItineraryDomain
import com.itenary.shared.domain.model.PriceDomain
import com.itenary.shared.domain.model.TimelineItemDomain
import com.itenary.shared.domain.model.TravelFromPrevDomain
import com.itenary.shared.domain.model.TravelMode
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

class ItineraryConverters {
    private val json = Json { ignoreUnknownKeys = true; explicitNulls = false }

    @TypeConverter
    fun toString(value: ItineraryDomain): String {
        val dto = value.toDto()
        return json.encodeToString(dto)
    }

    @TypeConverter
    fun fromString(value: String): ItineraryDomain {
        val dto = json.decodeFromString<ItineraryDto>(value)
        return dto.toDomain()
    }
}

// Serializable DTOs used for Room conversion to keep domain models untouched
@Serializable
private data class ItineraryDto(
    val days: List<DayDto> = emptyList(),
)

@Serializable
private data class DayDto(
    val dayIndex: Int,
    val dayLabel: String,
    val items: List<TimelineItemDto> = emptyList(),
)

@Serializable
private data class TimelineItemDto(
    val timeEnd: String = "",
    val timeStart: String = "",
    val lng: Float = 0f,
    val lat: Float = 0f,
    val expenseEstimation: PriceDto = PriceDto(),
    val pinpointMapsUrl: String = "",
    val name: String = "",
    val shortDesc: String = "",
    val category: String = "",
    val travelFromPrev: TravelFromPrevDto = TravelFromPrevDto(),
    val source: String = "",
    val confidence: Int = 0,
)

@Serializable
private data class PriceDto(
    val amount: Int = 0,
    val currency: String = "",
)

@Serializable
private data class TravelFromPrevDto(
    val mode: TravelModeDto = TravelModeDto.DEFAULT,
    val modeStr: String = "",
    val fareEstimate: PriceDto = PriceDto(),
    val distanceKm: Float = 0f,
    val durationMin: Int = 0,
)

@Serializable
private enum class TravelModeDto { WALK, TAXI, BIKE, CAR, RIDE_HAILING, OTHERS, DEFAULT }

// Mappers Domain <-> DTO
private fun ItineraryDomain.toDto() = ItineraryDto(
    days = days.map { it.toDto() }
)

private fun DayDomain.toDto() = DayDto(
    dayIndex = dayIndex,
    dayLabel = dayLabel,
    items = items.map { it.toDto() }
)

private fun TimelineItemDomain.toDto() = TimelineItemDto(
    timeEnd = timeEnd,
    timeStart = timeStart,
    lng = lng,
    lat = lat,
    expenseEstimation = expenseEstimation.toDto(),
    pinpointMapsUrl = pinpointMapsUrl,
    name = name,
    shortDesc = shortDesc,
    category = category,
    travelFromPrev = travelFromPrev.toDto(),
    source = source,
    confidence = confidence,
)

private fun PriceDomain.toDto() = PriceDto(
    amount = amount,
    currency = currency,
)

private fun TravelFromPrevDomain.toDto() = TravelFromPrevDto(
    mode = mode.toDto(),
    modeStr = modeStr,
    fareEstimate = fareEstimate.toDto(),
    distanceKm = distanceKm,
    durationMin = durationMin,
)

private fun TravelMode.toDto(): TravelModeDto = when (this) {
    TravelMode.WALK -> TravelModeDto.WALK
    TravelMode.TAXI -> TravelModeDto.TAXI
    TravelMode.BIKE -> TravelModeDto.BIKE
    TravelMode.CAR -> TravelModeDto.CAR
    TravelMode.RIDE_HAILING -> TravelModeDto.RIDE_HAILING
    TravelMode.OTHERS -> TravelModeDto.OTHERS
    TravelMode.DEFAULT -> TravelModeDto.DEFAULT
}

private fun ItineraryDto.toDomain() = ItineraryDomain(
    days = days.map { it.toDomain() }.toMutableList()
)

private fun DayDto.toDomain() = DayDomain(
    dayIndex = dayIndex,
    dayLabel = dayLabel,
    items = items.map { it.toDomain() }.toMutableList(),
)

private fun TimelineItemDto.toDomain() = TimelineItemDomain(
    timeEnd = timeEnd,
    timeStart = timeStart,
    lng = lng,
    lat = lat,
    expenseEstimation = expenseEstimation.toDomain(),
    pinpointMapsUrl = pinpointMapsUrl,
    name = name,
    shortDesc = shortDesc,
    category = category,
    travelFromPrev = travelFromPrev.toDomain(),
    source = source,
    confidence = confidence,
)

private fun PriceDto.toDomain() = PriceDomain(
    amount = amount,
    currency = currency,
)

private fun TravelFromPrevDto.toDomain() = TravelFromPrevDomain(
    mode = mode.toDomain(),
    modeStr = modeStr,
    fareEstimate = fareEstimate.toDomain(),
    distanceKm = distanceKm,
    durationMin = durationMin,
)

private fun TravelModeDto.toDomain(): TravelMode = when (this) {
    TravelModeDto.WALK -> TravelMode.WALK
    TravelModeDto.TAXI -> TravelMode.TAXI
    TravelModeDto.BIKE -> TravelMode.BIKE
    TravelModeDto.CAR -> TravelMode.CAR
    TravelModeDto.RIDE_HAILING -> TravelMode.RIDE_HAILING
    TravelModeDto.OTHERS -> TravelMode.OTHERS
    TravelModeDto.DEFAULT -> TravelMode.DEFAULT
}

