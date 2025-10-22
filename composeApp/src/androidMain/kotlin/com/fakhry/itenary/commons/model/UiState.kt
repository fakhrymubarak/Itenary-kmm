package com.fakhry.itenary.commons.model

sealed class UiState<out T>() {
    object Loading : UiState<Nothing>()
    object NetworkError : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Error(val message: String? = null) : UiState<Nothing>()
}
