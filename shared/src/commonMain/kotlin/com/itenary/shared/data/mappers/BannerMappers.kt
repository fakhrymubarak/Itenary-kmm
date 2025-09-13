package com.itenary.shared.data.mappers

import com.itenary.shared.data.models.responses.BannerResponse
import com.itenary.shared.data.models.responses.BannerResultResponse
import com.itenary.shared.di.BASE_URL
import com.itenary.shared.domain.model.BannerDomain

private fun BannerResponse.toDomain() = BannerDomain(
    bannerImageUrl = if (bannerImageUrl.isNotEmpty()) "$BASE_URL$bannerImageUrl" else "",
    title = title,
)

fun toBannerResultDomain(resp: BannerResultResponse) = resp.result.map { it.toDomain() }
