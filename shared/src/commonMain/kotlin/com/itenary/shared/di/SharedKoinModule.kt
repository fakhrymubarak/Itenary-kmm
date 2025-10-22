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
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

const val BASE_URL = "http://172.17.23.6:8080"

val SharedKoinModule = module {
    single { HttpClientFactory(enableLogging = false).create(get()) }
    single { DispatcherProvider() }

    singleOf(::PlatformHttpEngine) bind HttpClientEngineProvider::class
    factoryOf(::SSEEventMapper)

    factory<ItineraryStreamRepository> {
        ItineraryStreamRepositoryImpl(
            httpClient = get(),
            sseEventMapper = get(),
            baseUrl = BASE_URL,
            itineraryDao = get()
        )
    }
    factory<BannerRepository> { BannerRepositoryImpl(get(), BASE_URL) }

    factoryOf(::StreamItineraryUseCase)
}
