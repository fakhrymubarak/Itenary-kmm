package com.itenary.shared.di

import com.itenary.shared.data.remote.HttpClientEngineProvider
import com.itenary.shared.data.remote.HttpClientFactory
import com.itenary.shared.data.remote.ItineraryApi
import com.itenary.shared.data.remote.ItineraryApiImpl
import com.itenary.shared.data.repository.ItineraryRepositoryImpl
import com.itenary.shared.domain.repository.ItineraryRepository
import com.itenary.shared.domain.usecase.GenerateItineraryUseCase
import com.itenary.shared.data.remote.PlatformHttpEngine
import org.koin.core.qualifier.named
import org.koin.dsl.module

val SharedModule = module {
    single { HttpClientFactory(enableLogging = false).create(get()) }

    // Provide platform engine via expect/actual
    single<HttpClientEngineProvider> { PlatformHttpEngine() }

    single(named("baseUrl")) { "http://localhost:8080" }

    factory<ItineraryApi> { ItineraryApiImpl(get(), get(named("baseUrl"))) }

    single<ItineraryRepository> { ItineraryRepositoryImpl(get()) }

    factory { GenerateItineraryUseCase(get()) }
}


