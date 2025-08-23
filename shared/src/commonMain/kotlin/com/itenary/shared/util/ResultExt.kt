package com.itenary.shared.util

inline fun <T> resultOf(block: () -> T): Result<T> =
    try {
        Result.success(block())
    } catch (t: Throwable) {
        Result.failure(t)
    }

inline fun <T, R> Result<T>.mapCatching(transform: (T) -> R): Result<R> =
    fold(
        onSuccess = { value -> resultOf { transform(value) } },
        onFailure = { error -> Result.failure(error) }
    )

inline fun <T> Result<T>.onFailureCatching(block: (Throwable) -> Unit): Result<T> =
    also { if (isFailure) block(exceptionOrNull() as Throwable) }


