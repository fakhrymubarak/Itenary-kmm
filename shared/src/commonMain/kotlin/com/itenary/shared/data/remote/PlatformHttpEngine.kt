package com.itenary.shared.data.remote

expect class PlatformHttpEngine() : HttpClientEngineProvider {
    override fun provide(): io.ktor.client.engine.HttpClientEngineFactory<*>
}


