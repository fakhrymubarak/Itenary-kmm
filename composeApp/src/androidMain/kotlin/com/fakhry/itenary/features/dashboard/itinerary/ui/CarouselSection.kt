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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.fakhry.itenary.components.DotsIndicator
import com.fakhry.itenary.components.ShimmerBox
import com.fakhry.itenary.components.ShimmerCircle
import com.fakhry.itenary.features.dashboard.ui.UiState
import com.fakhry.itenary.theme.Spacing
import com.itenary.shared.domain.model.BannerDomain
import com.valentinilk.shimmer.shimmer

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
            is UiState.Error -> ErrorBannerSection(false, onRetrySection)
            UiState.NetworkError -> ErrorBannerSection(true, onRetrySection)
            is UiState.Success -> BannerCarousel(state.data, onClickBanner)
        }
    }
}

@Composable
fun BannerCarousel(banners: List<BannerDomain>, onClickBanner: (String) -> Unit) {
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
            AsyncImage(
                model = banners[page].bannerImageUrl,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .clickable { onClickBanner(banners[page].title) },
                contentScale = ContentScale.Crop,
            )
        }

        DotsIndicator(
            modifier = Modifier.align(Alignment.BottomCenter),
            totalDots = banners.size, selectedIndex = pagerState.currentPage
        )
    }
}


@Composable
fun ShimmerBannerSection() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .shimmer()
            .background(MaterialTheme.colorScheme.surfaceVariant)
    ) {
        // Add some content-like shimmer elements to make it more realistic
        Column(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(Spacing.md),
            verticalArrangement = Arrangement.spacedBy(Spacing.sm)
        ) {
            // Title shimmer
            ShimmerBox(modifier = Modifier.fillMaxWidth(0.6f), height = 24.dp)

            // Subtitle shimmer
            ShimmerBox(modifier = Modifier.fillMaxWidth(0.4f), height = 16.dp)
        }

        // Page indicators shimmer at bottom
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(Spacing.sm),
            horizontalArrangement = Arrangement.spacedBy(Spacing.xs)
        ) { repeat(3) { ShimmerCircle(size = 8.dp) } }
    }
}


@Composable
fun ErrorBannerSection(
    isNetwork: Boolean = true,
    onRetrySection: () -> Unit = {},
) {
    val message =
        if (isNetwork) "Network Error. Please try again." else "Something went wrong. Please try again."
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
                onRetrySection,
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

@Preview()
@Composable
private fun ShimmerBannerSectionPreview() {
    CarouselSection(state = UiState.Loading)
}

@Preview()
@Composable
private fun ErrorBannerSectionPreview() {
    CarouselSection(state = UiState.NetworkError)
}


@Preview()
@Composable
private fun BannerCarouselSectionPreview() {
    CarouselSection(state = UiState.Success(data = listOf(BannerDomain(), BannerDomain(), BannerDomain())))
}

