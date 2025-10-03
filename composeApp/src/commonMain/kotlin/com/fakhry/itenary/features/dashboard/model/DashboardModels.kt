package com.fakhry.itenary.features.dashboard.model

import com.fakhry.itenary.model.DayRequestUiModel
import com.fakhry.itenary.model.ItineraryRequestUiModel
import com.itenary.shared.domain.model.BannerDomain
import com.itenary.shared.domain.repository.DomainResult

enum class ValidationPlaceResult {
    INIT,
    VALID,
    INVALID_CANNOT_EMPTY,
    INVALID_LENGTH_TOO_SHORT,
    INVALID_LENGTH_TOO_LONG,
}

sealed class UiState<out T> {
    object Loading : UiState<Nothing>()
    object NetworkError : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Error(val message: String? = null) : UiState<Nothing>()
}

data class DashboardState(
    val itineraryRequest: ItineraryRequestUiModel = ItineraryRequestUiModel(days = listOf(DayRequestUiModel())),
    val validationPlaceResult: ValidationPlaceResult = ValidationPlaceResult.INIT,
    val banners: UiState<List<BannerDomain>> = UiState.Loading,
)

fun <T, R> DomainResult<T>.mapDomainResultToUiState(mapper: (T) -> R): UiState<R> = when (this) {
    is DomainResult.Error -> UiState.Error(message)
    DomainResult.NetworkError -> UiState.NetworkError
    is DomainResult.Success -> UiState.Success(mapper(data))
}

sealed class DashboardSideEffect {
    data class NavigateToResults(val request: ItineraryRequestUiModel) : DashboardSideEffect()
    data class ShowError(val message: String) : DashboardSideEffect()
}
