package com.fakhry.itenary.features.dashboard.itinerary.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import coil.compose.AsyncImage
import androidx.compose.ui.layout.ContentScale
import com.valentinilk.shimmer.shimmer

actual fun Modifier.platformShimmer(): Modifier = this.shimmer()

@Composable
actual fun BannerImage(url: String?, contentDescription: String?, modifier: Modifier) {
    AsyncImage(
        model = url,
        contentDescription = contentDescription,
        modifier = modifier,
        contentScale = ContentScale.Crop
    )
}
