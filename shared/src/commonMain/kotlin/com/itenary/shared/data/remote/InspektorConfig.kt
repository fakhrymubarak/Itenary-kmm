package com.itenary.shared.data.remote

import io.ktor.client.HttpClientConfig

// Expect a platform-specific implementation; Android installs Inspektor, iOS is no-op.
expect fun installInspektor(config: HttpClientConfig<*>)

