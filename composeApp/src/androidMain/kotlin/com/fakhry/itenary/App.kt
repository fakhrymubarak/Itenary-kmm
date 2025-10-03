package com.fakhry.itenary

import android.app.Application
import androidx.compose.runtime.Composable
import com.itenary.shared.di.SharedKoinModule
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

class ItineraryApp : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@ItineraryApp)
            printLogger(Level.DEBUG)
            modules(
                SharedKoinModule,
                module {
                    viewModelOf(::ItineraryViewModel)
                }
            )
        }
    }
}
