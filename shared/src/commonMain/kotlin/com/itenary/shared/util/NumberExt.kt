package com.itenary.shared.util

fun Int?.orZero() = this ?: 0

fun Long?.orZero() = this ?: 0L

fun Float?.orZero() = this ?: 0f

fun Double?.orZero() = this ?: 0.0

fun localizePrice(amount: Int, currency: String): String {
    return when {
        amount == 0 -> "Free"
        amount >= 1_000_000 -> {
            formatVeryLargeAmount(amount, currency)
        }

        amount >= 1000 -> {
            formatLargeAmount(amount, currency)
        }

        else -> formatSmallAmount(amount, currency)
    }
}

fun formatVeryLargeAmount(amount: Int, currency: String): String {
    val amountInM = amount / 1_000_000.0
    val formattedAmount = formatDecimal(amountInM)
    return when (currency.uppercase()) {
        "IDR" -> "Rp $formattedAmount M"
        "USD" -> "$$formattedAmount M"
        "EUR" -> "€$formattedAmount M"
        "GBP" -> "£$formattedAmount M"
        "JPY" -> "¥$formattedAmount M"
        else -> "$formattedAmount M $currency"
    }
}

fun formatLargeAmount(amount: Int, currency: String): String {
    val amountInK = amount / 1000.0
    val formattedAmount = formatDecimal(amountInK)
    return when (currency.uppercase()) {
        "IDR" -> "Rp $formattedAmount K"
        "USD" -> "$$formattedAmount K"
        "EUR" -> "€$formattedAmount K"
        "GBP" -> "£$formattedAmount K"
        "JPY" -> "¥$formattedAmount K"
        else -> "$formattedAmount K $currency"
    }
}

fun formatSmallAmount(amount: Int, currency: String): String {
    return when (currency.uppercase()) {
        "IDR" -> "Rp ${formatWithCommas(amount)}"
        "USD" -> "$${formatWithCommas(amount)}"
        "EUR" -> "€${formatWithCommas(amount)}"
        "GBP" -> "£${formatWithCommas(amount)}"
        "JPY" -> "¥${formatWithCommas(amount)}"
        else -> "${formatWithCommas(amount)} $currency"
    }
}

/** Formats a decimal number to show at most 1 decimal place, removing unnecessary zeros */
private fun formatDecimal(value: Double): String {
    return if (value == value.toInt().toDouble()) {
        value.toInt().toString()
    } else {
        val formatted = (value * 10).toInt() / 10.0
        formatted.toString().trimEnd('0').trimEnd('.')
    }
}

/** Formats an integer with comma separators for thousands */
private fun formatWithCommas(value: Int): String {
    return value.toString().reversed().chunked(3).joinToString(",").reversed()
}
