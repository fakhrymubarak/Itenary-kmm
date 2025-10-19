package com.itenary.shared.util

import com.itenary.shared.util.DomainResult
import io.ktor.client.call.body
import io.ktor.client.network.sockets.SocketTimeoutException
import io.ktor.client.plugins.ResponseException
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.isSuccess
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.coroutines.CancellationException
import kotlinx.io.IOException
import kotlinx.serialization.SerializationException

/**
 * Generic HTTP call handler that provides consistent error handling across all API calls.
 *
 * @param T The expected response type from the API
 * @param R The mapped domain type
 * @param httpCall The suspend function that makes the HTTP call
 * @param mapperResult Maps API response T to domain R
 */
suspend inline fun <reified T, R> safeCall(
    crossinline httpCall: suspend () -> HttpResponse,
    crossinline mapperResult: (T) -> R,
): DomainResult<R> {
    return try {
        val response = httpCall()
        if (response.status.isSuccess()) {
            val apiBody: T = response.body()
            DomainResult.Success(mapperResult(apiBody))
        } else {
            val code = response.status.value
            val raw = runCatching { response.bodyAsText() }.getOrNull().orEmpty()
            val message = raw.ifBlank { "HTTP $code ${response.status.description}" }
            DomainResult.Error(message = message, code = code)
        }
    } catch (e: CancellationException) {
        // VERY important: never swallow coroutine cancellation
        throw e
    } catch (e: ResponseException) {
        val code = e.response.status.value
        val raw = runCatching { e.response.bodyAsText() }.getOrNull().orEmpty()
        val message = raw.ifBlank { e.message ?: "HTTP $code" }
        DomainResult.Error(message = message, code = code)
    } catch (e: SerializationException) {
        DomainResult.Error(message = "Failed to parse response: ${e.message}", code = -1)
    } catch (_: UnresolvedAddressException) {
        DomainResult.NetworkError
    } catch (_: SocketTimeoutException) {
        DomainResult.NetworkError
    } catch (_: IOException) {
        DomainResult.NetworkError
    } catch (e: Exception) {
        DomainResult.Error(message = "Unexpected error: ${e.message}", code = -1)
    }
}
