package com.fakhry.itenary.features.dashboard.itinerary.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.fakhry.itenary.model.DayRequestUiModel
import com.fakhry.itenary.theme.Spacing
import kotlin.math.roundToInt

data class TransitionDayState(
    val day: DayRequestUiModel,
    val transitionState: MutableTransitionState<Boolean>,
)

fun LazyListScope.daysSection(
    tempDaysState: MutableState<List<TransitionDayState>>,
    onUpdateDayTime: (Int, DayRequestUiModel) -> Unit,
    onAddDay: (DayRequestUiModel) -> Unit,
    onRemoveDay: (List<DayRequestUiModel>) -> Unit,
) {
    itemsIndexed(
        items = tempDaysState.value,
        key = { _, d -> d.day.id },
    ) { index, dayState ->
        val visibleState = dayState.transitionState
        val exitFinished by remember { derivedStateOf { !visibleState.currentState && visibleState.isIdle } }

        LaunchedEffect(exitFinished) {
            if (exitFinished) {
                val id = dayState.day.id
                val newList = tempDaysState.value
                    .filterNot { it.day.id == id }
                    .mapIndexed { i, s -> s.copy(day = s.day.copy(day = i + 1)) }
                tempDaysState.value = newList
                onRemoveDay(newList.map { it.day })
            }
        }

        AnimatedVisibility(
            modifier = Modifier.padding(horizontal = Spacing.sm),
            visibleState = visibleState,
            enter = expandVertically(animationSpec = tween()),
            exit = shrinkVertically(animationSpec = tween()),
        ) {
            DayCard(
                day = dayState.day,
                onUpdateDay = { updated -> onUpdateDayTime(index, updated) },
                isShowRemoveIcon = index > 0,
                onRemove = { visibleState.targetState = false },
            )
        }
    }

    item { Spacer(modifier = Modifier.height(Spacing.sm)) }

    item {
        ElevatedButton(
            onClick = {
                val newTransitionState = MutableTransitionState(false)
                val currentListDay = tempDaysState.value.toMutableList()
                val newDayNumber = currentListDay.size + 1
                val nextId = (currentListDay.maxOfOrNull { it.day.id } ?: 0) + 1
                val newDay = DayRequestUiModel(id = nextId, day = newDayNumber)
                val newDayState = TransitionDayState(newDay, newTransitionState)
                currentListDay.add(newDayState)
                tempDaysState.value = currentListDay
                newTransitionState.targetState = true

                onAddDay(newDay)
            },
            modifier = Modifier
                .padding(horizontal = Spacing.sm)
                .height(32.dp)
        ) {
            Text("Add Day", style = MaterialTheme.typography.labelSmall)
        }
    }
}

@Composable
private fun DayCard(
    modifier: Modifier = Modifier,
    day: DayRequestUiModel,
    onUpdateDay: (DayRequestUiModel) -> Unit,
    isShowRemoveIcon: Boolean,
    onRemove: () -> Unit,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = Spacing.sm),
        colors = CardDefaults.outlinedCardColors(),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Spacing.md),
        ) {
            DayHeaderRow(
                dayNumber = day.day,
                startTime = day.startTime,
                endTime = day.endTime,
                onUpdateTimeWindow = { start, end -> onUpdateDay(day.copy(startTime = start, endTime = end)) },
                isShowRemoveIcon = isShowRemoveIcon,
                onRemove = onRemove
            )

            Spacer(modifier = Modifier.height(Spacing.sm))

            PaceSection(
                pace = day.pace,
                onPaceCommit = { onUpdateDay(day.copy(pace = it)) }
            )
        }
    }
}

@Composable
private fun DayHeaderRow(
    dayNumber: Int,
    startTime: String,
    endTime: String,
    onUpdateTimeWindow: (String, String) -> Unit,
    isShowRemoveIcon: Boolean,
    onRemove: () -> Unit,
) {
    var showStartTimePicker by remember { mutableStateOf(false) }
    var showEndTimePicker by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "Day $dayNumber",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(Spacing.sm),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ElevatedButton(
                onClick = { showStartTimePicker = true },
                contentPadding = PaddingValues(horizontal = Spacing.sm),
                modifier = Modifier.height(28.dp),
            ) { Text(startTime, style = MaterialTheme.typography.labelSmall) }

            ElevatedButton(
                onClick = { showEndTimePicker = true },
                contentPadding = PaddingValues(horizontal = Spacing.sm),
                modifier = Modifier.height(28.dp),
            ) { Text(endTime, style = MaterialTheme.typography.labelSmall) }

            if (isShowRemoveIcon) {
                IconButton(
                    onClick = onRemove,
                    interactionSource = remember { MutableInteractionSource() }
                ) {
                    Text("Remove", style = MaterialTheme.typography.labelSmall)
                }
            }
        }
    }

    if (showStartTimePicker) {
        TimeInputDialog(
            title = "Start time",
            initialTime = startTime,
            onDismiss = { showStartTimePicker = false },
            onConfirm = { newStart ->
                showStartTimePicker = false
                onUpdateTimeWindow(newStart, endTime)
            }
        )
    }

    if (showEndTimePicker) {
        TimeInputDialog(
            title = "End time",
            initialTime = endTime,
            onDismiss = { showEndTimePicker = false },
            onConfirm = { newEnd ->
                showEndTimePicker = false
                onUpdateTimeWindow(startTime, newEnd)
            }
        )
    }
}

@Composable
private fun PaceSection(
    pace: Float,
    onPaceCommit: (Float) -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    var sliderPosition by remember(pace) { mutableFloatStateOf(pace) }

    Column(verticalArrangement = Arrangement.spacedBy(Spacing.xs)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Day Pace", style = MaterialTheme.typography.labelMedium)
            Text(
                text = paceLabel(sliderPosition),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary
            )
        }

        Slider(
            modifier = Modifier.fillMaxWidth(),
            value = sliderPosition,
            valueRange = 1f..10f,
            onValueChange = { sliderPosition = it },
            onValueChangeFinished = { onPaceCommit(sliderPosition) },
            interactionSource = interactionSource,
            colors = SliderDefaults.colors(
                activeTrackColor = MaterialTheme.colorScheme.primary,
                inactiveTrackColor = MaterialTheme.colorScheme.primaryContainer
            )
        )

        Text(
            text = "1 = very relaxed, 10 = very packed",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

private fun paceLabel(value: Float): String {
    val rounded = value.roundToInt()
    return when {
        rounded <= 3 -> "Relaxed ($rounded)"
        rounded <= 7 -> "Balanced ($rounded)"
        else -> "Packed ($rounded)"
    }
}

@Composable
private fun TimeInputDialog(
    title: String,
    initialTime: String,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit,
) {
    var timeText by remember { mutableStateOf(initialTime) }
    val sanitized = remember(timeText) { timeText.trim() }
    val isValid = remember(sanitized) { TIME_REGEX.matches(sanitized) }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = { onConfirm(sanitized) }, enabled = isValid) {
                Text("Set")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        },
        title = { Text(title) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(Spacing.sm)) {
                TextField(
                    value = timeText,
                    onValueChange = { value ->
                        val filtered = value.filter { it.isDigit() || it == ':' }
                        if (filtered.length <= 5) {
                            timeText = filtered
                        }
                    },
                    label = { Text("HH:mm") },
                    singleLine = true,
                    supportingText = {
                        Text(
                            text = "Use 24-hour format",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                )
                if (!isValid && timeText.isNotBlank()) {
                    Text(
                        text = "Enter a time between 00:00 and 23:59",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error,
                        textAlign = TextAlign.Start
                    )
                }
            }
        }
    )
}
private val TIME_REGEX = Regex("^(?:[01]\\d|2[0-3]):[0-5]\\d$")
