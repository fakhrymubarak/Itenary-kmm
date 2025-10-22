package com.itenary.shared.data.remote

import io.ktor.client.HttpClientConfig

// Disabled Inspektor for compatibility with Kotlin 2.0.21
actual fun installInspektor(config: HttpClientConfig<*>) { /* no-op */ }
