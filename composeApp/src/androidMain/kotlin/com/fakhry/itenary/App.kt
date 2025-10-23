package com.fakhry.itenary

import android.app.Application
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.fakhry.itenary.features.dashboard.history.ui.HistoryResultsViewModel
import com.fakhry.itenary.features.dashboard.history.ui.details.HistoryDetailsViewModel
import com.fakhry.itenary.features.dashboard.navigation.DashboardRoute
import com.fakhry.itenary.features.dashboard.ui.DashboardViewModel
import com.fakhry.itenary.features.itinerary.results.ui.ItineraryViewModel
import com.fakhry.itenary.navigation.AppNavHost
import com.fakhry.itenary.theme.ItenaryTheme
import com.itenary.shared.di.AndroidDatabaseModule
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
            allowOverride(true)
            printLogger(Level.DEBUG)
            modules(
                SharedKoinModule,
                AndroidDatabaseModule,
                module {
                    viewModelOf(::ItineraryViewModel)
                    viewModelOf(::DashboardViewModel)
                    viewModelOf(::HistoryResultsViewModel)
                    viewModelOf(::HistoryDetailsViewModel)
                }
            )
        }
    }
}

@Composable
@Preview
fun App() {
    ItenaryTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            AppNavHost(
                navController = rememberNavController(),
                startDestination = DashboardRoute,
            )
        }
    }
}
