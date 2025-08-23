package com.fakhry.itenary

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.fakhry.itenary.navigation.NavigationViewModel
import com.fakhry.itenary.ui.dashboard.DashboardViewModel
import com.itenary.shared.di.SharedKoinModule
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.dsl.module

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        startKoin {
            androidContext(this@MainActivity)
            printLogger(Level.DEBUG)
            modules(
                SharedKoinModule,
                module {
                    viewModelOf(::ItineraryViewModel)
                    viewModelOf(::DashboardViewModel)
                    viewModelOf(::NavigationViewModel)
                }
            )
        }

        setContent {
            App()
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}