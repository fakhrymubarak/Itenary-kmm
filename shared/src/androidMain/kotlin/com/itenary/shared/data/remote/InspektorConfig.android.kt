package com.itenary.shared.data.remote

import com.gyanoba.inspektor.Inspektor
import com.gyanoba.inspektor.LogLevel.HEADERS
import com.gyanoba.inspektor.UnstableInspektorAPI
import io.ktor.client.HttpClientConfig
import kotlin.time.Duration.Companion.days

@OptIn(UnstableInspektorAPI::class)
actual fun installInspektor(config: HttpClientConfig<*>) {
    config.install(Inspektor) {
        level = HEADERS
        maxContentLength = 100_000
        filter { request -> request.url.host.contains("example.com") }
        sanitizeHeader { header -> header == "Authorization" }
        showNotifications = true
        retentionDuration = 30.days
    }
}

