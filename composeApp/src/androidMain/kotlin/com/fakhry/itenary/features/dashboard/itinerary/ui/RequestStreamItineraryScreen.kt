package com.fakhry.itenary.features.dashboard.itinerary.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.fakhry.itenary.commons.model.ItineraryRequestUiModel
import com.fakhry.itenary.features.dashboard.ui.DashboardSideEffect
import com.fakhry.itenary.features.dashboard.ui.DashboardViewModel
import com.fakhry.itenary.features.dashboard.ui.ValidationPlaceResult
import com.fakhry.itenary.theme.Spacing
import org.koin.androidx.compose.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RequestStreamItineraryScreen(
    viewModel: DashboardViewModel = koinViewModel(),
    onNavigateToDetails: (ItineraryRequestUiModel) -> Unit = {},
) {
    val state by viewModel.collectAsState()

    // Collect side effects using Orbit MVI
    LaunchedEffect(viewModel) {
        viewModel.container.sideEffectFlow.collect { sideEffect ->
            when (sideEffect) {
                is DashboardSideEffect.NavigateToResults -> {
                    onNavigateToDetails(sideEffect.request)
                }

                is DashboardSideEffect.ShowError -> {
//                    snackbarHostState.showSnackbar(sideEffect.message)
                }
            }
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        ItineraryAppBar()

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(Spacing.md),
            verticalArrangement = Arrangement.spacedBy(Spacing.lg)
        ) {
            item { CarouselSection() }

            item {
                LocationInputSection(
                    place = state.itineraryRequest.place,
                    onPlaceChange = viewModel::updatePlace,
                    validationPlaceResult = state.validationPlaceResult,
                )
            }

            item {
                DaysSection(
                    days = state.itineraryRequest.days,
                    onUpdateDayTime = viewModel::updateDayTime,
                    onAddDay = viewModel::addDay,
                    onRemoveDay = viewModel::removeDay,
                )
            }

            item {
                Button(
                    onClick = { viewModel.generateItinerary() },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = state.validationPlaceResult == ValidationPlaceResult.VALID
                ) {
                    Text("Generate Itinerary")
                }
            }
        }
    }
}

