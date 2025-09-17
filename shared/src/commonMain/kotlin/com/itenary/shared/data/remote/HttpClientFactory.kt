package com.itenary.shared.data.remote

import com.gyanoba.inspektor.Inspektor
import com.gyanoba.inspektor.LogLevel.HEADERS
import com.gyanoba.inspektor.UnstableInspektorAPI
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.sse.SSE
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import kotlin.time.Duration.Companion.days

class HttpClientFactory(
    private val enableLogging: Boolean = false,
) {
    @OptIn(UnstableInspektorAPI::class)
    fun create(engine: HttpClientEngineProvider): HttpClient {
        return HttpClient(engine.provide()) {
            install(Inspektor) {
                level = HEADERS
                maxContentLength = 100_000
                filter { request -> request.url.host.contains("example.com") }
                sanitizeHeader { header -> header == "Authorization" }
                showNotifications = true
                retentionDuration = 30.days
            }
            install(ContentNegotiation) {
                json(
                    Json {
                        ignoreUnknownKeys = true
                        explicitNulls = false
                        isLenient = true
                    })
            }
            install(SSE)
            install(HttpTimeout) {
                connectTimeoutMillis = 15_000
                requestTimeoutMillis = 30_000
                socketTimeoutMillis = 30_000
            }
            if (enableLogging) {
                install(Logging) {
                    logger = object : Logger {
                        override fun log(message: String) {
                            println(message)
                        }
                    }
                    level = LogLevel.ALL
                }
            }
        }
    }
}

interface HttpClientEngineProvider {
    fun provide(): HttpClientEngineFactory<*>
}


