package com.fakhry.itenary.commons.ui

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.fakhry.itenary.theme.Spacing
import com.valentinilk.shimmer.shimmer

@Composable
fun ListItemShimmer(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(Spacing.md),
        horizontalArrangement = Arrangement.spacedBy(Spacing.md)
    ) {
        // Avatar shimmer
        Box(
            modifier =
                Modifier
                    .size(48.dp)
                    .shimmer()
                    .background(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = CircleShape
                    )
        )

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
                Box(
                    modifier =
                        Modifier
                            .size(32.dp)
                            .shimmer()
                            .background(
                                color = MaterialTheme.colorScheme.surfaceVariant,
                                shape = CircleShape
                            )
                )
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
