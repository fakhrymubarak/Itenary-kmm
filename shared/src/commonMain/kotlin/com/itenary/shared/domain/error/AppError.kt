package com.itenary.shared.domain.error

sealed class AppError(message: String? = null, cause: Throwable? = null) : Throwable(message, cause) {
    class Network(cause: Throwable) : AppError("Network error", cause)
    class Server(val code: String?, val httpCode: Int, message: String) : AppError(message)
    class Serialization(cause: Throwable) : AppError("Serialization error", cause)
    class Validation(message: String) : AppError(message)
    class Unknown(cause: Throwable) : AppError("Unknown error", cause)
}


