package com.fakhry.itenary.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.fakhry.itenary.theme.Spacing

@Composable
fun ShimmerBox(modifier: Modifier = Modifier, height: androidx.compose.ui.unit.Dp = 20.dp) {
    val infiniteTransition = rememberInfiniteTransition(label = "shimmer")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = 0.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "shimmer_alpha"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .clip(RoundedCornerShape(4.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .graphicsLayer { this.alpha = alpha }
    )
}

@Composable
fun ShimmerCircle(modifier: Modifier = Modifier, size: androidx.compose.ui.unit.Dp = 8.dp) {
    val infiniteTransition = rememberInfiniteTransition(label = "shimmer")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = 0.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "shimmer_alpha"
    )

    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .graphicsLayer { this.alpha = alpha }
    )
}

@Composable
fun ListItemShimmer(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(Spacing.md),
        horizontalArrangement = Arrangement.spacedBy(Spacing.md)
    ) {
        // Avatar shimmer
        ShimmerCircle(size = 48.dp)

        // Content shimmer
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(Spacing.xs)
        ) {
            ShimmerBox(modifier = Modifier.fillMaxWidth(0.8f), height = 16.dp)
            ShimmerBox(modifier = Modifier.fillMaxWidth(0.6f), height = 14.dp)
        }
    }
}

@Composable
fun CardShimmer(modifier: Modifier = Modifier, showAvatar: Boolean = true) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(Spacing.md)
    ) {
        if (showAvatar) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(Spacing.sm)
            ) {
                ShimmerCircle(size = 32.dp)
                ShimmerBox(modifier = Modifier.width(120.dp), height = 16.dp)
            }
        }

        Spacer(modifier = Modifier.height(Spacing.sm))

        // Card content
        repeat(3) {
            ShimmerBox(modifier = Modifier.fillMaxWidth(), height = 14.dp)
            Spacer(modifier = Modifier.height(Spacing.xs))
        }

        ShimmerBox(modifier = Modifier.fillMaxWidth(0.7f), height = 14.dp)
    }
}
