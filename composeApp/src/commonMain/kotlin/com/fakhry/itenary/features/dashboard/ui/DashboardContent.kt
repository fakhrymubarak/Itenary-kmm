package com.fakhry.itenary.features.dashboard.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.fakhry.itenary.model.ItineraryRequestUiModel

@Composable
fun DashboardContent(
    innerPadding: PaddingValues,
    onNavigateToDetails: (ItineraryRequestUiModel) -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Dashboard Content",
            style = MaterialTheme.typography.headlineMedium
        )
    }
}
