package com.fakhry.itenary.features.dashboard.history.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.fakhry.itenary.theme.ItenaryTheme
import com.fakhry.itenary.theme.Spacing


@Composable
fun HistoryErrorState(
    message: String,
    onRetry: () -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(Spacing.md)
    ) {
        Text(
            text = "❌", style = MaterialTheme.typography.displayMedium
        )
        Text(
            text = message, style = MaterialTheme.typography.bodyLarge, textAlign = TextAlign.Center
        )
        Button(onClick = onRetry) {
            Text("Retry")
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HistoryErrorStatePreview() {
    ItenaryTheme {
        HistoryErrorState(
            message = "Something went wrong while fetching the data.", onRetry = {})
    }
}
