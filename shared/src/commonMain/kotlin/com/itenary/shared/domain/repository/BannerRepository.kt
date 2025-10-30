package com.itenary.shared.domain.repository

import com.itenary.shared.domain.model.BannerDomain
import com.itenary.shared.util.DomainResult

interface BannerRepository {
    suspend fun getBanners(): DomainResult<List<BannerDomain>>
}
