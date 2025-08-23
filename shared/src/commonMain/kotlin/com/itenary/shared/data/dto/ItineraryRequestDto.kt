package com.itenary.shared.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class ItineraryRequestDto(
    val city: String,
    val province: String,
    val countryCode: String,
    val startDate: String,
    val endDate: String,
    val language: String
)


