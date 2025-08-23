package com.itenary.shared.data.remote

import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.engine.darwin.Darwin

actual class PlatformHttpEngine actual constructor() : HttpClientEngineProvider {
    actual override fun provide(): HttpClientEngineFactory<*> = Darwin
}


