package com.fakhry.itenary.features.dashboard.itinerary.ui

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.fakhry.itenary.commons.model.DayRequestUiModel
import com.fakhry.itenary.theme.ItenaryTheme
import com.fakhry.itenary.theme.Spacing
import java.time.LocalTime
import java.time.format.DateTimeFormatter

fun LazyListScope.daysSection(
    days: List<DayRequestUiModel>,
    onUpdateDayTime: (Int, DayRequestUiModel) -> Unit,
    onAddDay: () -> Unit,
    onRemoveDay: (Int) -> Unit,
    key: (DayRequestUiModel) -> Any = { it.day }, // Prefer a stable unique id if you have one
) {
    itemsIndexed(items = days, key = { _, d -> key(d) }) { index, day ->
        DayCard(
            modifier = Modifier.padding(horizontal = Spacing.sm),
            day = day,
            onUpdateDay = { onUpdateDayTime(index, it) },
            onRemove = if (days.size > 1) ({ onRemoveDay(index) }) else null,
        )
    }

    item {
        ElevatedButton(
            onClick = onAddDay, modifier = Modifier
                .padding(horizontal = Spacing.sm)
                .height(32.dp)
        ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "Add Day")
            Text("Add More Day", style = MaterialTheme.typography.labelSmall)
        }
    }
}


@Composable
fun DayCard(
    modifier: Modifier = Modifier,
    day: DayRequestUiModel,
    onUpdateDay: (DayRequestUiModel) -> Unit,
    onRemove: (() -> Unit)?,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.outlinedCardColors(),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Spacing.md),
        ) {
            DayHeaderRow(
                day.day, day.startTime, day.endTime, onUpdateTimeWindow = { start, end ->
                    onUpdateDay(day.copy(startTime = start, endTime = end))
                }, onRemove
            )

            Spacer(modifier = Modifier.height(Spacing.sm))

            PaceSection(pace = day.pace, onPaceCommit = { onUpdateDay(day.copy(pace = it)) })
        }
    }
}

@Composable
private fun DayHeaderRow(
    dayNumber: Int,
    startTime: String,
    endTime: String,
    onUpdateTimeWindow: (String, String) -> Unit,
    onRemove: (() -> Unit)?,
) {
    var showStartTimePicker by remember { mutableStateOf(false) }
    var showEndTimePicker by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "Day $dayNumber", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(Spacing.sm), verticalAlignment = Alignment.CenterVertically
        ) {
            // Start Time Button
            ElevatedButton(
                onClick = { showStartTimePicker = true },
                contentPadding = PaddingValues(0.dp),
                modifier = Modifier.height(24.dp),
            ) { Text(startTime, style = MaterialTheme.typography.labelSmall) }

            // End Time Button
            ElevatedButton(
                onClick = { showEndTimePicker = true },
                contentPadding = PaddingValues(0.dp),
                modifier = Modifier.height(24.dp),
            ) { Text(endTime, style = MaterialTheme.typography.labelSmall) }

            // Remove Button (only show if more than 1 day)
            onRemove?.let { removeAction ->
                IconButton(onClick = removeAction, modifier = Modifier.size(20.dp)) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Remove Day",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }

    if (showStartTimePicker) {
        val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
        val currentEnd = LocalTime.parse(endTime, timeFormatter)
        TimePickerDialog(
            initialTime = startTime,
            onTimeSelected = {
                showStartTimePicker = false
                if (startTime == it) return@TimePickerDialog
                onUpdateTimeWindow(it, endTime)
            },
            onDismiss = { showStartTimePicker = false },
            confirmEnabled = { candidate -> candidate.isBefore(currentEnd) },
        )
    }

    if (showEndTimePicker) {
        val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
        val currentStart = LocalTime.parse(startTime, timeFormatter)
        TimePickerDialog(
            initialTime = endTime,
            onTimeSelected = {
                showEndTimePicker = false
                if (endTime == it) return@TimePickerDialog
                onUpdateTimeWindow(startTime, it)
            },
            onDismiss = { showEndTimePicker = false },
            confirmEnabled = { candidate -> candidate.isAfter(currentStart) },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerDialog(
    initialTime: String,
    onTimeSelected: (String) -> Unit,
    onDismiss: () -> Unit,
    confirmEnabled: (LocalTime) -> Boolean = { true },
) {
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
    val initialLocalTime = LocalTime.parse(initialTime, timeFormatter)

    val timePickerState = rememberTimePickerState(
        initialHour = initialLocalTime.hour, initialMinute = initialLocalTime.minute
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Select Time") },
        text = { TimePicker(state = timePickerState) },
        confirmButton = {
            TextButton(
                enabled = confirmEnabled(LocalTime.of(timePickerState.hour, timePickerState.minute)), onClick = {
                    val selectedTime = LocalTime.of(timePickerState.hour, timePickerState.minute)
                    onTimeSelected(selectedTime.format(timeFormatter))
                }) { Text("OK") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } })
}


@Composable
fun PaceSection(
    pace: Float,
    onPaceCommit: (Float) -> Unit,
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = "Pace", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.width(Spacing.md))

        Column {
            Row {
                Text(
                    text = "Relaxed",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.SemiBold,
                )

                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = "Fast",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.SemiBold,
                )
            }

            Pace(pace, onPaceCommit)

            Row {
                Text(
                    text = "\uD83D\uDC22",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.SemiBold,
                )

                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = "\uD83D\uDC07",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.SemiBold,
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Pace(
    pace: Float,
    onPaceCommit: (Float) -> Unit,
) {
    var tempPace by remember(pace) { mutableFloatStateOf(pace) }
    Slider(
        modifier = Modifier
            .fillMaxWidth()
            .height(30.dp),
        value = tempPace,
        onValueChange = { tempPace = it },
        onValueChangeFinished = {
            if (tempPace != pace) onPaceCommit(tempPace)
        },
        valueRange = 1f..10f,
        thumb = {
            SliderDefaults.Thumb(
                interactionSource = remember { MutableInteractionSource() },
                colors = SliderDefaults.colors(),
                thumbSize = DpSize(Spacing.md, Spacing.md)
            )
        },
        track = { sliderPositions ->
            val colors = SliderDefaults.colors()
            SliderDefaults.Track(
                modifier = Modifier.height(Spacing.sm), // custom track thickness
                enabled = true,
                colors = colors,
                sliderState = sliderPositions,
                drawStopIndicator = {},
                drawTick = { _, _ -> })
        })

}

@Composable
@Preview(showBackground = true)
fun DaysSectionPreview() {
    ItenaryTheme {

        LazyColumn {
            daysSection(
                days = listOf(DayRequestUiModel(), DayRequestUiModel()),
                onUpdateDayTime = { _, _ -> },
                onAddDay = {},
                onRemoveDay = { },
            )
        }
    }
}
