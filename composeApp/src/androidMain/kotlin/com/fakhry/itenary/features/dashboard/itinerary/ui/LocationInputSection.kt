package com.fakhry.itenary.features.dashboard.itinerary.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Map
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.fakhry.itenary.features.dashboard.ui.ValidationPlaceResult

@Composable
fun LocationInputSection(
    modifier: Modifier = Modifier,
    place: String,
    onPlaceChange: (String) -> Unit,
    validationPlaceResult: ValidationPlaceResult,
) {
    val isError =
        validationPlaceResult != ValidationPlaceResult.VALID && validationPlaceResult != ValidationPlaceResult.INIT

    Column {
        OutlinedTextField(
            modifier = modifier.fillMaxWidth(),
            value = place,
            onValueChange = onPlaceChange,
            label = { Text("Where to?") },
            leadingIcon = {
                Icon(imageVector = Icons.Rounded.Map, contentDescription = "Location")
            },
            singleLine = true,
            isError = isError,
        )

        if (isError) {
            val errorMessage = when (validationPlaceResult) {
                ValidationPlaceResult.INVALID_CANNOT_EMPTY -> "Place cannot be empty"
                ValidationPlaceResult.INVALID_LENGTH_TOO_SHORT -> "Place name must be at least 2 characters"
                ValidationPlaceResult.INVALID_LENGTH_TOO_LONG -> "Place name must be less than 100 characters"
                else -> ""
            }
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.Companion.padding(start = 16.dp, top = 4.dp)
            )
        }
    }
}
