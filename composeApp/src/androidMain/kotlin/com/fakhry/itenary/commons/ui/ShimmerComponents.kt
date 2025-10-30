package com.fakhry.itenary.commons.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.valentinilk.shimmer.shimmer

@Composable
fun ShimmerBox(modifier: Modifier = Modifier, height: androidx.compose.ui.unit.Dp = 20.dp) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .shimmer()
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant, shape = RoundedCornerShape(4.dp)
            )
    )
}

@Composable
fun ShimmerCircle(modifier: Modifier = Modifier, size: androidx.compose.ui.unit.Dp = 8.dp) {
    Box(
        modifier = modifier
            .size(size)
            .shimmer()
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant, shape = RoundedCornerShape(size / 2)
            )
    )
}
