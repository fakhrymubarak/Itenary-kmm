package com.fakhry.itenary.features.itinerary.results.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.fakhry.itenary.commons.model.ItineraryRequestUiModel
import com.fakhry.itenary.features.itinerary.results.model.StreamingDayUiModel
import com.fakhry.itenary.theme.ItenaryTheme
import com.fakhry.itenary.theme.Spacing
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultStreamItineraryScreen(
    itineraryRequestUiModel: ItineraryRequestUiModel,
    onNavigateBack: () -> Unit,
    viewModel: ItineraryViewModel = koinViewModel(),
) {
    LaunchedEffect(Unit) { viewModel.streamItineraryGeneration(itineraryRequestUiModel) }
    val state by viewModel.streamingUiState.collectAsStateWithLifecycle(
        initialValue = viewModel.latestState
    )
    val snackbarHostState = remember { SnackbarHostState() }

    val listState = rememberLazyListState()

    Scaffold(topBar = {
        TopAppBar(
            title = { Text("Itinerary Results") }, navigationIcon = {
                IconButton(onClick = onNavigateBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back"
                    )
                }
            }, colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        )
    }, snackbarHost = { SnackbarHost(snackbarHostState) }) { paddingValues ->
        LaunchedEffect(state.saveSuccess) {
            if (state.saveSuccess) {
                snackbarHostState.showSnackbar("Itinerary saved locally")
                viewModel.consumeSaveResult()
            }
        }
        LaunchedEffect(state.saveError, state.saveConflict) {
            val msg = state.saveError
            if (!state.saveConflict && !msg.isNullOrBlank()) {
                snackbarHostState.showSnackbar(msg)
                viewModel.consumeSaveError()
            }
        }
        if (state.saveConflict) {
            SaveConflictDialog(
                onOverwrite = viewModel::overwriteExistingItinerary,
                onSaveCopy = viewModel::keepBothItinerary,
                onCancel = viewModel::consumeSaveConflict,
            )
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                state.isLoading -> {
                    // Loading state
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator()
                        Spacer(modifier = Modifier.height(Spacing.md))
                        Text(
                            text = "Generating your itinerary...", style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }

                state.streamingItinerary.days.isNotEmpty() -> {
                    // Streaming results
                    val streamingItinerary = state.streamingItinerary
                    val totalTimelineItems = streamingItinerary.days.sumOf { it.items.size }
                    val totalLazyItems = streamingItinerary.days.size
                    val lastItem = if (totalLazyItems - 2 < 0) 0 else totalLazyItems - 2
                    LaunchedEffect(totalLazyItems, totalTimelineItems) {
                        if (totalLazyItems > 0) {
                            listState.animateScrollToItem(
                                lastItem, scrollOffset = Int.MAX_VALUE
                            )
                        }
                    }
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(Spacing.md),
                        verticalArrangement = Arrangement.spacedBy(Spacing.md),
                        state = listState
                    ) {
                        items(streamingItinerary.days) { day -> DayCard(day = day) }

                        // Show completion status
                        if (streamingItinerary.isComplete) {
                            item {
                                Card(
                                    modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(
                                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                                    )
                                ) {
                                    Column(modifier = Modifier.padding(Spacing.md)) {
                                        Text(
                                            text = "✅ Itinerary Complete!",
                                            style = MaterialTheme.typography.headlineSmall,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Spacer(modifier = Modifier.height(Spacing.sm))
                                        Text(
                                            text = "Your personalized itinerary is ready. You can now plan your trip!",
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                    }
                                }
                            }

                            item {
                                Button(
                                    modifier = Modifier.fillMaxWidth(),
                                    onClick = viewModel::saveToLocalStorage,
                                ) {
                                    Text("Save Itinerary", softWrap = false)
                                }
                            }
                        }
                    }
                }

                state.error != null -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(Spacing.md),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "❌ Error",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(Spacing.md))
                        Text(
                            text = state.error ?: "Something went wrong", style = MaterialTheme.typography.bodyLarge
                        )
                        Spacer(modifier = Modifier.height(Spacing.lg))
                        Button(onClick = onNavigateBack) { Text("Go Back") }
                    }
                }

                else -> return@Box
            }
        }
    }
}

@Composable
fun DayCard(
    day: StreamingDayUiModel,
) {
    Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)) {
        Column(modifier = Modifier.padding(Spacing.md)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = day.dayLabel, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold
                )
                if (day.isComplete) {
                    Text(
                        text = "✅ Complete",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                } else {
                    Text(
                        text = "⏳ In Progress",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(Spacing.sm))

            // Show timeline items
            TimelineSection(items = day.items)

            if (day.items.isEmpty() && !day.isComplete) {
                Text(
                    text = "Generating activities...",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun SaveConflictDialog(
    onOverwrite: () -> Unit = {},
    onSaveCopy: () -> Unit = {},
    onCancel: () -> Unit = {},
) {
    AlertDialog(
        onDismissRequest = onCancel,
        icon = {
            Icon(
                imageVector = Icons.Rounded.Warning,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        },
        title = {
            Text(
                text = "Itinerary for this place has been added",
                style = MaterialTheme.typography.titleLarge
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    text = "This place already has an itinerary. Do you want to keep both or replace the existing one?",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        },
        confirmButton = {
            Column {
                Button(
                    onClick = { onSaveCopy() },
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text("Keep Both")
                }

                OutlinedButton(
                    onClick = onOverwrite,
                    modifier = Modifier.fillMaxWidth(),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.error),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Overwrite Existing")
                }
                TextButton(
                    onClick = onCancel,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Cancel")
                }
            }
        },
        shape = MaterialTheme.shapes.extraLarge
    )
}

@Preview(showBackground = true)
@Composable
private fun SaveConflictDialogPreview() {
    ItenaryTheme {
        SaveConflictDialog()
    }
}
