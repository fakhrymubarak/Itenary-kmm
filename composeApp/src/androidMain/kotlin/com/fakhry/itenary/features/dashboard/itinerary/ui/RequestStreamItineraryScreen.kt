package com.fakhry.itenary.features.dashboard.itinerary.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.fakhry.itenary.R
import com.fakhry.itenary.commons.model.DayRequestUiModel
import com.fakhry.itenary.commons.model.ItineraryRequestUiModel
import com.fakhry.itenary.features.dashboard.ui.DashboardSideEffect
import com.fakhry.itenary.features.dashboard.ui.DashboardViewModel
import com.fakhry.itenary.theme.Spacing
import org.koin.androidx.compose.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import java.time.LocalTime
import java.time.format.DateTimeFormatter

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

                is DashboardSideEffect.ShowSuccess -> {
//                    snackbarHostState.showSnackbar(sideEffect.message)
                }
            }
        }
    }


    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
    ) {
        TopAppBar(
            title = {
                Image(
                    painter = painterResource(id = R.drawable.ic_logo_landscape),
                    contentDescription = "App Logo",
                )
            },
            colors =
                TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
        )

        // Main Content - Scrollable
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(Spacing.md),
            verticalArrangement = Arrangement.spacedBy(Spacing.lg)
        ) {
            item { CarouselSection() }

            item {
                // Location Input
                LocationInputSection(
                    place = state.itineraryRequest.place,
                    onPlaceChange = viewModel::updatePlace,
                    error = state.validationErrors["place"]
                )
            }

            item {
                // Days Section
                DaysSection(
                    days = state.itineraryRequest.days,
                    onUpdateDayTime = viewModel::updateDayTime,
                    onAddDay = viewModel::addDay,
                    onRemoveDay = viewModel::removeDay
                )
            }

            item {
                // Generate Button
                Button(
                    onClick = { viewModel.generateItinerary() },
                    modifier = Modifier.fillMaxWidth(),
                    enabled =
                        !state.isLoading &&
                            state.itineraryRequest.place.isNotBlank() &&
                            state.itineraryRequest.days.isNotEmpty()
                ) {
                    if (state.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    } else {
                        Text("Generate Itinerary")
                    }
                }
            }
        }
    }

}

@Composable
fun LocationInputSection(place: String, onPlaceChange: (String) -> Unit, error: String? = null) {
    Column {
        OutlinedTextField(
            value = place,
            onValueChange = onPlaceChange,
            label = { Text("Where to?") },
            leadingIcon = {
                Icon(imageVector = Icons.Default.LocationOn, contentDescription = "Location")
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            isError = error != null
        )

        // Show validation error
        error?.let { errorMessage ->
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }
    }
}

@Composable
fun DaysSection(
    days: List<DayRequestUiModel>,
    onUpdateDayTime: (Int, String, String) -> Unit,
    onAddDay: () -> Unit,
    onRemoveDay: (Int) -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(Spacing.md)) {
        days.forEachIndexed { index, dayRequest ->
            DayCard(
                dayNumber = dayRequest.day,
                startTime = dayRequest.startTime,
                endTime = dayRequest.endTime,
                onStartTimeChange = { newTime ->
                    onUpdateDayTime(index, newTime, dayRequest.endTime)
                },
                onEndTimeChange = { newTime ->
                    onUpdateDayTime(index, dayRequest.startTime, newTime)
                },
                onRemove =
                    if (days.size > 1) {
                        { onRemoveDay(index) }
                    } else null
            )
        }

        // Add More Day Button
        OutlinedButton(onClick = onAddDay, modifier = Modifier.fillMaxWidth()) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "Add Day")
            Spacer(modifier = Modifier.width(Spacing.xs))
            Text("Add More Day")
        }
    }
}

@Composable
fun DayCard(
    dayNumber: Int,
    startTime: String,
    endTime: String,
    onStartTimeChange: (String) -> Unit,
    onEndTimeChange: (String) -> Unit,
    onRemove: (() -> Unit)?,
) {
    var showStartTimePicker by remember { mutableStateOf(false) }
    var showEndTimePicker by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Spacing.md),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Day $dayNumber",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Row(horizontalArrangement = Arrangement.spacedBy(Spacing.sm)) {
                // Start Time Button
                OutlinedButton(onClick = { showStartTimePicker = true }) { Text(startTime) }

                // End Time Button
                OutlinedButton(onClick = { showEndTimePicker = true }) { Text(endTime) }

                // Remove Button (only show if more than 1 day)
                onRemove?.let { removeAction ->
                    IconButton(onClick = removeAction) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Remove Day",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        }
    }

    // Time Pickers
    if (showStartTimePicker) {
        TimePickerDialog(
            initialTime = startTime,
            onTimeSelected = { time ->
                onStartTimeChange(time)
                showStartTimePicker = false
            },
            onDismiss = { showStartTimePicker = false }
        )
    }

    if (showEndTimePicker) {
        TimePickerDialog(
            initialTime = endTime,
            onTimeSelected = { time ->
                onEndTimeChange(time)
                showEndTimePicker = false
            },
            onDismiss = { showEndTimePicker = false }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerDialog(
    initialTime: String,
    onTimeSelected: (String) -> Unit,
    onDismiss: () -> Unit,
) {
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
    val initialLocalTime = LocalTime.parse(initialTime, timeFormatter)

    val timePickerState =
        rememberTimePickerState(
            initialHour = initialLocalTime.hour,
            initialMinute = initialLocalTime.minute
        )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Select Time") },
        text = { TimePicker(state = timePickerState) },
        confirmButton = {
            TextButton(
                onClick = {
                    val selectedTime = LocalTime.of(timePickerState.hour, timePickerState.minute)
                    onTimeSelected(selectedTime.format(timeFormatter))
                }
            ) { Text("OK") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
    )
}
