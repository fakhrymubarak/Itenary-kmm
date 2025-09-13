package com.itenary.shared.data.models.responses

import kotlinx.serialization.Serializable

@Serializable
data class BannerResponse(
    val bannerImageUrl: String = "",
    val title: String = "",
)

@Serializable
data class BannerResultResponse(
    val result: List<BannerResponse> = emptyList(),
)
