package com.fakhry.itenary.features.dashboard.ui

import androidx.compose.foundation.background
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.fakhry.itenary.features.dashboard.itinerary.ui.CarouselSection
import com.fakhry.itenary.features.dashboard.itinerary.ui.ItineraryAppBar
import com.fakhry.itenary.features.dashboard.itinerary.ui.LocationInputSection
import com.fakhry.itenary.features.dashboard.itinerary.ui.TransitionDayState
import com.fakhry.itenary.features.dashboard.itinerary.ui.daysSection
import com.fakhry.itenary.features.dashboard.model.DashboardSideEffect
import com.fakhry.itenary.features.dashboard.viewmodel.DashboardScreenModel
import com.fakhry.itenary.features.dashboard.viewmodel.isGenerateEnabled
import com.fakhry.itenary.model.DayRequestUiModel
import com.fakhry.itenary.model.ItineraryRequestUiModel
import com.fakhry.itenary.theme.Spacing
import com.itenary.shared.domain.repository.BannerRepository
import com.itenary.shared.util.DispatcherProvider
import org.koin.compose.koinInject

@Composable
fun RequestStreamItineraryScreen(
    onNavigateToDetails: (ItineraryRequestUiModel) -> Unit,
    screenModel: DashboardScreenModel = rememberDashboardScreenModel(),
) {
    val state by screenModel.uiState.collectAsState()
    val sideEffectFlow = screenModel.sideEffect

    LaunchedEffect(sideEffectFlow) {
        sideEffectFlow.collect { sideEffect ->
            when (sideEffect) {
                is DashboardSideEffect.NavigateToResults -> onNavigateToDetails(sideEffect.request)
                is DashboardSideEffect.ShowError -> Unit
            }
        }
    }

    val tempDaysState = remember(state.itineraryRequest.days) {
        mutableStateOf(
            state.itineraryRequest.days.ifEmpty { listOf(DayRequestUiModel(id = 0, day = 1)) }.map {
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
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = Spacing.md),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Spacing.md)
        ) {
            item {
                CarouselSection(
                    state = state.banners,
                    onRetrySection = screenModel::loadBanners,
                    onClickBanner = screenModel::updatePlace
                )
            }

            item {
                LocationInputSection(
                    place = state.itineraryRequest.place,
                    onPlaceChange = screenModel::updatePlace,
                    validationPlaceResult = state.validationPlaceResult
                )
            }

            daysSection(
                tempDaysState = tempDaysState,
                onUpdateDayTime = screenModel::updateDayTime,
                onAddDay = screenModel::addDay,
                onRemoveDay = screenModel::removeDay,
            )

            item {
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = Spacing.sm),
                    onClick = screenModel::generateItinerary,
                    enabled = state.isGenerateEnabled()
                ) {
                    Text("Generate Itinerary")
                }
            }
        }
    }
}

@Composable
fun rememberDashboardScreenModel(): DashboardScreenModel {
    val bannerRepository: BannerRepository = koinInject()
    val dispatcher: DispatcherProvider = koinInject()
    val screenModel = remember { DashboardScreenModel(bannerRepository, dispatcher) }
    DisposableEffect(Unit) {
        onDispose { screenModel.onDispose() }
    }
    return screenModel
}
