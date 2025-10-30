package com.itenary.shared.data.remote

import com.itenary.shared.data.mappers.toBannerResultDomain
import com.itenary.shared.data.models.responses.BannerResultResponse
import com.itenary.shared.domain.model.BannerDomain
import com.itenary.shared.domain.repository.BannerRepository
import com.itenary.shared.util.safeCall
import io.ktor.client.HttpClient
import io.ktor.client.request.accept
import io.ktor.client.request.get
import io.ktor.http.ContentType
import io.ktor.http.path
import io.ktor.http.takeFrom

class BannerRepositoryImpl(
    private val httpClient: HttpClient,
    private val baseUrl: String,
) : BannerRepository {

    override suspend fun getBanners() = safeCall<BannerResultResponse, List<BannerDomain>>(
        httpCall = {
            httpClient.get {
                url {
                    takeFrom(baseUrl)
                    path("api/v1/banner")
                }
                accept(ContentType.Application.Json)
            }
        },
        mapperResult = ::toBannerResultDomain,
    )
}
