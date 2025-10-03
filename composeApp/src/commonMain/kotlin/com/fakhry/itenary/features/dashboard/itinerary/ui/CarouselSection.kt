package com.fakhry.itenary.features.dashboard.itinerary.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.fakhry.itenary.components.DotsIndicator
import com.fakhry.itenary.components.ShimmerBox
import com.fakhry.itenary.components.ShimmerCircle
import com.fakhry.itenary.features.dashboard.model.UiState
import com.fakhry.itenary.theme.Spacing
import com.itenary.shared.domain.model.BannerDomain

@Composable
fun CarouselSection(
    modifier: Modifier = Modifier,
    state: UiState<List<BannerDomain>>,
    onClickBanner: (String) -> Unit = {},
    onRetrySection: () -> Unit = {},
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(786f / 336f),
        contentAlignment = Alignment.Center,
    ) {
        when (state) {
            UiState.Loading -> ShimmerBannerSection()
            is UiState.Error -> ErrorBannerSection(isNetwork = false, onRetrySection = onRetrySection)
            UiState.NetworkError -> ErrorBannerSection(isNetwork = true, onRetrySection = onRetrySection)
            is UiState.Success -> BannerCarousel(state.data, onClickBanner)
        }
    }
}

@Composable
private fun BannerCarousel(banners: List<BannerDomain>, onClickBanner: (String) -> Unit) {
    if (banners.isEmpty()) {
        EmptyCarouselPlaceholder()
        return
    }

    val pagerState = rememberPagerState(pageCount = { banners.size })

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        HorizontalPager(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .align(Alignment.Center),
            state = pagerState
        ) { page ->
            val banner = banners[page]
            BannerImage(
                url = banner.bannerImageUrl,
                contentDescription = banner.title,
                modifier = Modifier
                    .fillMaxSize()
                    .clickable { onClickBanner(banner.title) },
            )
        }

        DotsIndicator(
            modifier = Modifier.align(Alignment.BottomCenter),
            totalDots = banners.size,
            selectedIndex = pagerState.currentPage
        )
    }
}

@Composable
private fun EmptyCarouselPlaceholder() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surfaceVariant)
    )
}

@Composable
private fun ShimmerBannerSection() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .platformShimmer()
            .background(MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(Spacing.md),
            verticalArrangement = Arrangement.spacedBy(Spacing.sm)
        ) {
            ShimmerBox(modifier = Modifier.fillMaxWidth(0.6f), height = 24.dp)
            ShimmerBox(modifier = Modifier.fillMaxWidth(0.4f), height = 16.dp)
        }

        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(Spacing.sm),
            horizontalArrangement = Arrangement.spacedBy(Spacing.xs)
        ) {
            repeat(3) { ShimmerCircle(size = 8.dp) }
        }
    }
}

@Composable
private fun ErrorBannerSection(
    isNetwork: Boolean,
    onRetrySection: () -> Unit,
) {
    val message = if (isNetwork) {
        "Network error. Please try again."
    } else {
        "Something went wrong. Please try again."
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.errorContainer),
        contentAlignment = Alignment.Center,
    ) {
        Row(
            modifier = Modifier.padding(Spacing.md),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(message, modifier = Modifier.weight(1f))
            Spacer(Modifier.width(Spacing.sm))
            Button(
                onClick = onRetrySection,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error,
                    contentColor = MaterialTheme.colorScheme.onError,
                )
            ) {
                Text("Try Again", softWrap = false)
            }
        }
    }
}

expect fun Modifier.platformShimmer(): Modifier

@Composable
expect fun BannerImage(url: String?, contentDescription: String?, modifier: Modifier)
