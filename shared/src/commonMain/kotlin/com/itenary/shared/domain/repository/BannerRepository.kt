package com.itenary.shared.domain.repository

import com.itenary.shared.domain.model.BannerDomain

sealed class DomainResult<out T>() {
    data class Success<T>(val data: T) : DomainResult<T>()
    data class Error(val message: String? = null, val code: Int = -1) : DomainResult<Nothing>()
    object NetworkError : DomainResult<Nothing>()
}

interface BannerRepository {
    suspend fun getBanners(): DomainResult<List<BannerDomain>>
}
