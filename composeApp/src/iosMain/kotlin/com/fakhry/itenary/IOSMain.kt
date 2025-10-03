package com.fakhry.itenary

import com.itenary.shared.di.SharedKoinModule
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.dsl.module

internal actual fun initPlatformKoin() {
    startKoin {
        printLogger(Level.DEBUG)
        modules(
            SharedKoinModule,
            module {
                // iOS-specific modules can be added here
            }
        )
    }
}
