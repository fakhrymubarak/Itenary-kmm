package com.fakhry.itenary.features.dashboard.itinerary.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.fakhry.itenary.features.dashboard.model.ValidationPlaceResult
import com.fakhry.itenary.theme.Spacing

@Composable
fun LocationInputSection(
    place: String,
    onPlaceChange: (String) -> Unit,
    validationPlaceResult: ValidationPlaceResult,
) {
    val isError = validationPlaceResult != ValidationPlaceResult.VALID && validationPlaceResult != ValidationPlaceResult.INIT

    Column(modifier = Modifier.padding(horizontal = Spacing.sm)) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = place,
            onValueChange = onPlaceChange,
            label = { Text("Where to?") },
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
            if (errorMessage.isNotBlank()) {
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = Spacing.xs)
                )
            }
        }
    }
}
