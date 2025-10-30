package com.fakhry.itenary.features.dashboard.itinerary.ui

import androidx.compose.foundation.Image
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import com.fakhry.itenary.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItineraryAppBar() {
    TopAppBar(
        title = {
            Image(
                painter = painterResource(id = R.drawable.ic_logo_landscape),
                contentDescription = "App Logo",
            )
        }, colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    )
}
