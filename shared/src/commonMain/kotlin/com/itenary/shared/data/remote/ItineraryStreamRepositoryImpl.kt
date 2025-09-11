package com.itenary.shared.data.remote

import com.itenary.shared.data.mappers.SSEEventMapper
import com.itenary.shared.data.mappers.toDto
import com.itenary.shared.data.models.responses.SSEEventDto
import com.itenary.shared.domain.model.ItineraryRequest
import com.itenary.shared.domain.model.StreamingEvent
import com.itenary.shared.domain.repository.ItineraryStreamRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.http.headers
import io.ktor.http.isSuccess
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.readUTF8Line
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

// TODO NEED TO UPDATE DATA STREAMINGLY,
class ItineraryStreamRepositoryImpl(
    private val httpClient: HttpClient,
    private val sseEventMapper: SSEEventMapper,
    private val baseUrl: String,
) : ItineraryStreamRepository {

    override suspend fun streamItineraryGeneration(
        request: ItineraryRequest,
    ): Flow<StreamingEvent> = flow {
        try {
            val response: HttpResponse =
                httpClient.post {
                    headers {
                        append(HttpHeaders.Accept, "text/event-stream")
//                        append(HttpHeaders.Connection, "keep-alive")
//                        append(HttpHeaders.TransferEncoding, "chunked")
                    }
                    url("$baseUrl/sse/generate-itinerary")
                    contentType(ContentType.Application.Json)
                    setBody(request.toDto())
                }
            println("Trace response.status ${response.status.isSuccess()}")

            if (!response.status.isSuccess()) {
                emit(StreamingEvent.Error("HTTP ${response.status.value}: ${response.status.description}"))
                return@flow
            }


            val channel: ByteReadChannel = response.body()
            response.headers.forEach { body, tes ->
                println("Trace response.headers $body $tes")

            }
            parseSSEStream(channel) { sseEvent ->
                println("Trace SSEEvent $sseEvent")
                try {
                    val streamingEvent = sseEventMapper.mapToStreamingEvent(sseEvent)
                    emit(streamingEvent)
                } catch (e: Exception) {
                    emit(StreamingEvent.Error("Failed to parse SSE event: ${e.message}"))
                }
            }
        } catch (e: Exception) {
            emit(StreamingEvent.Error("Streaming failed: ${e.message}"))
        }
    }

    private suspend fun parseSSEStream(
        channel: ByteReadChannel,
        onEvent: suspend (SSEEventDto) -> Unit,
    ) {
        var currentEvent: String? = null
        val dataBuffer = StringBuilder()

        while (!channel.isClosedForRead) {
            println("Trace SSEEvent parseSSEStream readUTF8Line null")
            val line = channel.readUTF8Line() ?: break
            println("Trace SSEEvent parseSSEStream $line")

            when {
                line.startsWith("event: ") -> {
                    currentEvent = line.removePrefix("event: ").trim()
                }

                line.startsWith("data: ") -> {
                    val data = line.removePrefix("data: ").trim()
                    if (data.isNotEmpty()) {
                        if (dataBuffer.isNotEmpty()) {
                            dataBuffer.append('\n')
                        }
                        dataBuffer.append(data)
                    }
                }

                line.isEmpty() -> {
                    // Empty line indicates end of event - emit if we have both event and data
                    if (currentEvent != null && dataBuffer.isNotEmpty()) {
                        onEvent(SSEEventDto(event = currentEvent, data = dataBuffer.toString()))
                    }
                    // Reset for next event
                    currentEvent = null
                    dataBuffer.clear()
                }
            }
        }

        // Emit any remaining event if stream ends without empty line
        if (currentEvent != null && dataBuffer.isNotEmpty()) {
            onEvent(SSEEventDto(event = currentEvent, data = dataBuffer.toString()))
        }
    }
}
