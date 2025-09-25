package com.fakhry.itenary.features.dashboard.itinerary.ui

import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.fakhry.itenary.model.DayRequestUiModel
import com.fakhry.itenary.model.ItineraryRequestUiModel
import com.fakhry.itenary.features.dashboard.ui.DashboardSideEffect
import com.fakhry.itenary.features.dashboard.ui.DashboardViewModel
import com.fakhry.itenary.features.dashboard.ui.ValidationPlaceResult
import com.fakhry.itenary.theme.ItenaryTheme
import com.fakhry.itenary.theme.Spacing
import org.koin.androidx.compose.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState

data class TransitionDayState(
    val day: DayRequestUiModel,
    val transitionState: MutableTransitionState<Boolean>,
)

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
                is DashboardSideEffect.NavigateToResults -> onNavigateToDetails(sideEffect.request)
                is DashboardSideEffect.ShowError -> {}
            }
        }
    }

    val tempDaysState = remember {
        mutableStateOf(
            state.itineraryRequest.days.map {
                TransitionDayState(
                    day = it,
                    transitionState = MutableTransitionState(true)
                )
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        ItineraryAppBar()
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            item {
                CarouselSection(
                    state = state.banners,
                    onClickBanner = viewModel::updatePlace,
                    onRetrySection = viewModel::loadBanners,
                )
            }

            item { Spacer(modifier = Modifier.height(Spacing.md)) }

            item {
                LocationInputSection(
                    place = state.itineraryRequest.place,
                    onPlaceChange = viewModel::updatePlace,
                    validationPlaceResult = state.validationPlaceResult,
                )
            }
            item { Spacer(modifier = Modifier.height(Spacing.md)) }

            daysSection(
                tempDaysState = tempDaysState,
                onUpdateDayTime = viewModel::updateDayTime,
                onAddDay = viewModel::addDay,
                onRemoveDay = viewModel::removeDay,
            )

            item { Spacer(modifier = Modifier.height(Spacing.md)) }

            item {
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = Spacing.sm),
                    onClick = viewModel::generateItinerary,
                    enabled = state.validationPlaceResult == ValidationPlaceResult.VALID
                ) { Text("Generate Itinerary") }
            }
        }
    }
}

@Preview
@Composable
private fun RequestStreamItineraryScreenPreview() {
    ItenaryTheme {
        RequestStreamItineraryScreen()
    }
}
