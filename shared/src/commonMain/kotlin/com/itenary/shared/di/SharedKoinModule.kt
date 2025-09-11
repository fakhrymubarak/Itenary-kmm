package com.itenary.shared.di

import com.itenary.shared.data.mappers.SSEEventMapper
import com.itenary.shared.data.remote.HttpClientEngineProvider
import com.itenary.shared.data.remote.HttpClientFactory
import com.itenary.shared.data.remote.ItineraryStreamRepositoryImpl
import com.itenary.shared.data.remote.PlatformHttpEngine
import com.itenary.shared.domain.repository.ItineraryStreamRepository
import com.itenary.shared.domain.usecase.StreamItineraryUseCase
import com.itenary.shared.util.DispatcherProvider
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module

const val BASE_URL = "http://10.0.2.2:8080"

val SharedKoinModule = module {
    single { HttpClientFactory(enableLogging = false) }

    single(named("regular")) { get<HttpClientFactory>().create(get()) }
    single(named("cio")) { get<HttpClientFactory>().createCio() }

    single { DispatcherProvider() }

    singleOf(::PlatformHttpEngine) bind HttpClientEngineProvider::class
    factoryOf(::SSEEventMapper)

    factory<ItineraryStreamRepository> {
        ItineraryStreamRepositoryImpl(get(named("cio")), get(), BASE_URL)
    }

    factoryOf(::StreamItineraryUseCase)
}
