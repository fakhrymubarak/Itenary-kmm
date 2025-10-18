package com.itenary.shared.data.remote

import io.ktor.client.HttpClientConfig

actual fun installInspektor(config: HttpClientConfig<*>) {
    // No-op on iOS
}

