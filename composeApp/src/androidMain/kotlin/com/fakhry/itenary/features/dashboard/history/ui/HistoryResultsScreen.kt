package com.fakhry.itenary.features.dashboard.history.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.fakhry.itenary.features.dashboard.history.model.DeleteStatus
import com.fakhry.itenary.features.dashboard.history.model.HistoryItineraryUiModel
import com.fakhry.itenary.features.dashboard.history.state.HistoryResultUiState
import com.fakhry.itenary.features.dashboard.history.ui.components.HistoryErrorState
import com.fakhry.itenary.theme.ItenaryTheme
import com.fakhry.itenary.theme.Spacing
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryResultsScreen(
    onNavigateBack: () -> Unit,
    onItinerarySelected: (String) -> Unit,
    viewModel: HistoryResultsViewModel = koinViewModel(),
) {
    val fetchHistoryState by viewModel.fetchState.collectAsStateWithLifecycle()

    Scaffold(
        modifier = Modifier.fillMaxSize(), containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "History", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Transparent,
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                    navigationIconContentColor = MaterialTheme.colorScheme.onBackground
                ),
            )
        },
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                            MaterialTheme.colorScheme.background
                        )
                    )
                )
                .padding(innerPadding)
                .padding(horizontal = Spacing.md), contentAlignment = Alignment.Center
        ) {
            when (fetchHistoryState) {
                is HistoryResultUiState.Loading -> HistoryLoadingState()
                is HistoryResultUiState.Success -> HistoryItineraryList(
                    itineraries = (fetchHistoryState as HistoryResultUiState.Success).itineraries,
                    onItinerarySelected = onItinerarySelected,
                    onItineraryDeleteRequest = viewModel::requestDeleteItinerary,
                    onConfirmDeletion = viewModel::confirmDeleteRequest,
                    onDismissDeletion = viewModel::cancelDeleteRequest,
                )

                is HistoryResultUiState.Error -> HistoryErrorState(
                    message = (fetchHistoryState as HistoryResultUiState.Error).message,
                    onRetry = viewModel::loadCachedItineraries,
                )
            }
        }
    }
}

@Composable
private fun HistoryLoadingState() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(Spacing.md)
    ) {
        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
        Text(
            text = "Loading saved itineraries...",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun HistoryItineraryList(
    itineraries: List<HistoryItineraryUiModel>,
    onItinerarySelected: (String) -> Unit,
    onItineraryDeleteRequest: (HistoryItineraryUiModel) -> Unit,
    onConfirmDeletion: () -> Unit = {},
    onDismissDeletion: () -> Unit = {},
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(top = Spacing.md, bottom = Spacing.xxl),
        verticalArrangement = Arrangement.spacedBy(Spacing.lg)
    ) {
        items(
            items = itineraries, key = { itinerary -> itinerary.key }) { itinerary ->
            HistoryItineraryDismissibleCard(
                itinerary = itinerary,
                onClick = { onItinerarySelected(itinerary.key) },
                onDeleteRequest = { onItineraryDeleteRequest(itinerary) },
                onConfirmDeletion = onConfirmDeletion,
                onDismissDeletion = onDismissDeletion,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HistoryItineraryDismissibleCard(
    itinerary: HistoryItineraryUiModel,
    onClick: () -> Unit,
    onDeleteRequest: () -> Unit,
    onConfirmDeletion: () -> Unit = {},
    onDismissDeletion: () -> Unit = {},
) {
    val shouldShowAlert = itinerary.deletionStatus == DeleteStatus.SHOW_ALERT_USER
    val initialValue = if (shouldShowAlert) SwipeToDismissBoxValue.EndToStart else SwipeToDismissBoxValue.Settled
    val dismissState = rememberSwipeToDismissBoxState(
        initialValue = initialValue,
        confirmValueChange = { value ->
            if (value == SwipeToDismissBoxValue.EndToStart) {
                onDeleteRequest()
            }
            !shouldShowAlert
        },
        positionalThreshold = { distance -> distance * 0.5f },
    )


    if (shouldShowAlert) {
        HistoryDeleteConfirmationDialog(
            itinerary = itinerary,
            onConfirm = onConfirmDeletion,
            onDismiss = onDismissDeletion
        )
    }

    LaunchedEffect(shouldShowAlert) {
        if (itinerary.deletionStatus == DeleteStatus.CANCELLED) dismissState.reset()
    }

    SwipeToDismissBox(
        state = dismissState,
        enableDismissFromStartToEnd = false,
        modifier = Modifier.fillMaxWidth(),
        backgroundContent = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = Spacing.sm / 2)
                    .clip(MaterialTheme.shapes.large)
                    .background(MaterialTheme.colorScheme.errorContainer)
                    .padding(horizontal = Spacing.lg),
                contentAlignment = Alignment.CenterEnd
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(Spacing.sm)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onErrorContainer
                    )
                    Text(
                        text = "Delete",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                }
            }
        }) {
        HistoryItineraryCard(
            itinerary = itinerary, onClick = onClick
        )
    }
}

@Composable
private fun HistoryDeleteConfirmationDialog(
    itinerary: HistoryItineraryUiModel = HistoryItineraryUiModel(),
    onConfirm: () -> Unit = {},
    onDismiss: () -> Unit = {},
) {
    AlertDialog(onDismissRequest = onDismiss, title = {
        Text(
            text = "Delete itinerary?", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold
        )
    }, text = {
        Text(
            text = "Are you sure you want to delete \"${itinerary.title}\"?",
            style = MaterialTheme.typography.bodyMedium
        )
    }, confirmButton = {
        OutlinedButton(
            onClick = onConfirm,
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.error),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = MaterialTheme.colorScheme.error
            )
        ) {
            Text("Delete")
        }
    }, dismissButton = {
        TextButton(onClick = onDismiss) {
            Text(text = "Cancel")
        }
    })
}

@Composable
private fun HistoryItineraryCard(
    itinerary: HistoryItineraryUiModel = HistoryItineraryUiModel(),
    onClick: () -> Unit = {},
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Box(
            modifier = Modifier
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.08f),
                            MaterialTheme.colorScheme.secondary.copy(alpha = 0.08f)
                        )
                    )
                )
                .padding(Spacing.lg),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = itinerary.title,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(Spacing.sm))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(Spacing.sm)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Schedule,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = itinerary.totalDays,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.W400,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.weight(1f))

                    }
                }

                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HistoryItineraryCardPreview() {
    ItenaryTheme {
        HistoryItineraryCard(
            HistoryItineraryUiModel(
                key = "Komodo_Indonesia",
                title = "Komodo Indonesia",
                totalDays = "3 Days",
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun HistoryDeleteConfirmationDialogPreview() {
    ItenaryTheme {
        HistoryDeleteConfirmationDialog()
    }
}
