package com.fakhry.itenary.features.dashboard.itinerary.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
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

@Composable
fun DaysSection(
    modifier: Modifier = Modifier,
    days: List<DayRequestUiModel>,
    onUpdateDayTime: (Int, String, String, Float) -> Unit,
    onAddDay: () -> Unit,
    onRemoveDay: (Int) -> Unit,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(Spacing.sm),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        days.forEachIndexed { index, dayRequest ->
            DayCard(
                dayNumber = dayRequest.day,
                startTime = dayRequest.startTime,
                endTime = dayRequest.endTime,
                onStartTimeChange = { newTime ->
                    onUpdateDayTime(index, newTime, dayRequest.endTime, dayRequest.pace)
                },
                onEndTimeChange = { newTime ->
                    onUpdateDayTime(index, dayRequest.startTime, newTime, dayRequest.pace)
                },
                onRemove = if (days.size > 1) {
                    { onRemoveDay(index) }
                } else null,
                pace = dayRequest.pace,
                onPaceChange = { newPace ->
                    onUpdateDayTime(index, dayRequest.startTime, dayRequest.endTime, newPace)
                }
            )
        }

        // Add More Day Button
        ElevatedButton(
            onClick = onAddDay,
            contentPadding = ButtonDefaults.ButtonWithIconContentPadding
        ) {
            Icon(
                modifier = Modifier.size(18.dp),
                imageVector = Icons.Default.Add,
                contentDescription = "Add Day"
            )
            Spacer(modifier = Modifier.Companion.width(Spacing.xs))
            Text("Add More Day", style = MaterialTheme.typography.labelSmall)
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
    pace: Float,
    onPaceChange: (Float) -> Unit,
    onRemove: (() -> Unit)?,
) {
    var showStartTimePicker by remember { mutableStateOf(false) }
    var showEndTimePicker by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.Companion.fillMaxWidth(),
        colors = CardDefaults.outlinedCardColors(),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface),
    ) {
        Column(
            modifier = Modifier.Companion
                .fillMaxWidth()
                .padding(Spacing.md),
        ) {
            Row(
                modifier = Modifier.Companion.fillMaxWidth(),
                verticalAlignment = Alignment.Companion.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Day $dayNumber",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Companion.Bold
                )
                Row(horizontalArrangement = Arrangement.spacedBy(Spacing.sm)) {
                    // Start Time Button
                    ElevatedButton(
                        onClick = { showStartTimePicker = true },
                        contentPadding = ButtonDefaults.TextButtonContentPadding
                    ) {
                        Text(
                            startTime,
                            style = MaterialTheme.typography.labelSmall
                        )
                    }

                    // End Time Button
                    ElevatedButton(
                        onClick = { showEndTimePicker = true },
                        contentPadding = ButtonDefaults.TextButtonContentPadding
                    ) {
                        Text(
                            endTime,
                            style = MaterialTheme.typography.labelSmall
                        )
                    }

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

            Spacer(modifier = Modifier.height(Spacing.sm))

            Pace(pace = pace, onPaceChange = onPaceChange)
        }
    }

    // Time Pickers
    if (showStartTimePicker) {
        TimePickerDialog(initialTime = startTime, onTimeSelected = { time ->
            onStartTimeChange(time)
            showStartTimePicker = false
        }, onDismiss = { showStartTimePicker = false })
    }

    if (showEndTimePicker) {
        TimePickerDialog(initialTime = endTime, onTimeSelected = { time ->
            onEndTimeChange(time)
            showEndTimePicker = false
        }, onDismiss = { showEndTimePicker = false })
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

    val timePickerState = rememberTimePickerState(
        initialHour = initialLocalTime.hour, initialMinute = initialLocalTime.minute
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
                }) {
                Text("OK")
            }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Pace(
    pace: Float,
    onPaceChange: (Float) -> Unit,
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = "Pace", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Companion.SemiBold
        )

        Spacer(modifier = Modifier.width(Spacing.md))

        Column {
            Row {
                Text(
                    text = "Relaxed",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Companion.SemiBold
                )

                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = "Fast",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Companion.SemiBold
                )
            }

            Slider(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(30.dp),
                value = pace,
                onValueChange = onPaceChange,
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
                        drawStopIndicator = { },
                        drawTick = { _, _ -> })
                })

            Row {
                Text(
                    text = "\uD83D\uDC22",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Companion.SemiBold
                )

                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = "\uD83D\uDC07",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Companion.SemiBold
                )
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun DaysSectionPreview() {
    ItenaryTheme {
        DaysSection(
            days = listOf(DayRequestUiModel()),
            onUpdateDayTime = { _, _, _, _ -> },
            onAddDay = {},
            onRemoveDay = {},
        )
    }
}
