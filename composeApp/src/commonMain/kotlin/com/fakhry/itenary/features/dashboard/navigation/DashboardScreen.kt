package com.fakhry.itenary.features.dashboard.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions

object DashboardTab : Tab {

    override val options: TabOptions
        @Composable
        get() = remember { TabOptions(index = 0u, title = "Dashboard", icon = null) }

    @Composable
    override fun Content() {
        DashboardScreen()
    }
}

@Composable
fun DashboardScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Dashboard shared UI placeholder",
            style = MaterialTheme.typography.headlineMedium
        )
    }
}
