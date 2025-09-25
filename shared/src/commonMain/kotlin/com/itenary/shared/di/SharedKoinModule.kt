package com.itenary.shared.di

import com.itenary.shared.data.mappers.SSEEventMapper
import com.itenary.shared.data.remote.BannerRepositoryImpl
import com.itenary.shared.data.remote.HttpClientEngineProvider
import com.itenary.shared.data.remote.HttpClientFactory
import com.itenary.shared.data.remote.ItineraryStreamRepositoryImpl
import com.itenary.shared.data.remote.PlatformHttpEngine
import com.itenary.shared.domain.repository.BannerRepository
import com.itenary.shared.domain.repository.ItineraryStreamRepository
import com.itenary.shared.domain.usecase.StreamItineraryUseCase
import com.itenary.shared.util.DispatcherProvider
import io.ktor.client.HttpClient
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

const val BASE_URL = "http://172.17.22.128:8080"
val SharedKoinModule = module {
    single<HttpClient> { HttpClientFactory(true).create(get()) }
    single { DispatcherProvider() }

    singleOf(::PlatformHttpEngine) bind HttpClientEngineProvider::class
    factoryOf(::SSEEventMapper)

    factory<ItineraryStreamRepository> { ItineraryStreamRepositoryImpl(get(), get(), BASE_URL) }
    factory<BannerRepository> { BannerRepositoryImpl(get(), BASE_URL) }

    factoryOf(::StreamItineraryUseCase)
}
