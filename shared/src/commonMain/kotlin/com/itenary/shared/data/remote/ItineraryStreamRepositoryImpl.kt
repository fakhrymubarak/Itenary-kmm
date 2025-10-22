package com.itenary.shared.data.remote
import com.itenary.shared.data.database.ItineraryDao
import com.itenary.shared.data.database.ItineraryEntity
import com.itenary.shared.data.mappers.SSEEventMapper
import com.itenary.shared.data.mappers.toDto
import com.itenary.shared.domain.model.ItineraryDomain
import com.itenary.shared.domain.model.ItineraryRequest
import com.itenary.shared.domain.model.StreamingEvent
import com.itenary.shared.domain.repository.ItineraryStreamRepository
import com.itenary.shared.util.DomainResult
import io.ktor.client.HttpClient
import io.ktor.client.plugins.sse.sse
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.http.contentType
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class ItineraryStreamRepositoryImpl(
    private val httpClient: HttpClient,
    private val sseEventMapper: SSEEventMapper,
    private val baseUrl: String,
    private val itineraryDao: ItineraryDao,
) : ItineraryStreamRepository {

    override suspend fun streamItineraryGeneration(
        request: ItineraryRequest,
    ): Flow<StreamingEvent> = flow {
        try {
            httpClient.sse(
                request = {
                    method = HttpMethod.Post
                    url("$baseUrl/sse/generate-itinerary")
                    contentType(ContentType.Application.Json)
                    setBody(request.toDto())
                }
            ) {
                incoming.map { sseEventMapper.mapToStreamingEvent(it) }.collect { event ->
                    emit(event)
                }
            }
        } catch (e: Exception) {
            emit(StreamingEvent.Error("Streaming failed: ${e.message}"))
        }
    }

    override suspend fun cacheItinerary(key: String, itinerary: ItineraryDomain): DomainResult<Boolean> =
        try {
            val id = itineraryDao.insert(ItineraryEntity(id = key, itinerary = itinerary))
            if (id > 0) DomainResult.Success(true) else DomainResult.Error("Failed to cache itinerary")
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            DomainResult.Error(message = "DB error: ${e.message}")
        }
}
