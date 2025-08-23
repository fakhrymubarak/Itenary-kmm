package com.itenary.shared.data.remote

import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.engine.okhttp.OkHttp

actual class PlatformHttpEngine actual constructor() : HttpClientEngineProvider {
    actual override fun provide(): HttpClientEngineFactory<*> = OkHttp
}


