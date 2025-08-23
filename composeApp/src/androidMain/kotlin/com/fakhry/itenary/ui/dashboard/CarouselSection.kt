package com.fakhry.itenary.ui.dashboard

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.fakhry.itenary.R
import com.fakhry.itenary.theme.Spacing
import kotlinx.coroutines.delay

@Composable
fun CarouselSection() {
    val carouselImages = listOf(
        R.drawable.ic_launcher_background, // Using launcher as placeholder
        R.drawable.ic_launcher_background,
        R.drawable.ic_launcher_background
    )

    var currentIndex by remember { mutableIntStateOf(0) }

    LaunchedEffect(currentIndex) {
        delay(3000)
        currentIndex = (currentIndex + 1) % carouselImages.size
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(393f / 168f)
            .clip(RoundedCornerShape(12.dp))
    ) {
        Image(
            painter = painterResource(id = carouselImages[currentIndex]),
            contentDescription = "Carousel Image $currentIndex",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Page indicators
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(Spacing.sm),
            horizontalArrangement = Arrangement.spacedBy(Spacing.xs)
        ) {
            carouselImages.forEachIndexed { index, _ ->
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(
                            if (index == currentIndex)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                        )
                )
            }
        }
    }
}