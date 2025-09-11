package com.itenary.shared.util

fun Int?.orZero() = this ?: 0
fun Long?.orZero() = this ?: 0L
fun Float?.orZero() = this ?: 0f
fun Double?.orZero() = this ?: 0.0
