package com.itenary.shared.data.remote

import com.itenary.shared.data.dto.ErrorEnvelopeDto
import com.itenary.shared.data.dto.ItineraryRequestDto
import com.itenary.shared.data.dto.ItineraryResponseDto
import com.itenary.shared.domain.error.AppError
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.isSuccess
import io.ktor.http.contentType
import kotlinx.serialization.json.Json
import kotlinx.serialization.decodeFromString

interface ItineraryApi {
    suspend fun generate(dto: ItineraryRequestDto): ItineraryResponseDto
}

class ItineraryApiImpl(
    private val httpClient: HttpClient,
    private val baseUrl: String
) : ItineraryApi {

    override suspend fun generate(dto: ItineraryRequestDto): ItineraryResponseDto {
        val response: HttpResponse = httpClient.post("$baseUrl/api/v1/itinerary/generate") {
            contentType(ContentType.Application.Json)
            setBody(dto)
        }

        if (!response.status.isSuccess()) {
            throw response.toServerError()
        }
        return response.body()
    }

    private suspend fun HttpResponse.toServerError(): AppError.Server {
        val httpCode = status.value
        val raw = runCatching { bodyAsText() }.getOrDefault("")
        val json = Json { ignoreUnknownKeys = true; explicitNulls = false }
        val parsed = runCatching { json.decodeFromString<ErrorEnvelopeDto>(raw) }.getOrNull()
        val code = parsed?.error?.code
        val message = parsed?.error?.message?.ifBlank { raw }.orEmpty().ifBlank { "HTTP $httpCode" }
        return AppError.Server(code = code, httpCode = httpCode, message = message)
    }
}


