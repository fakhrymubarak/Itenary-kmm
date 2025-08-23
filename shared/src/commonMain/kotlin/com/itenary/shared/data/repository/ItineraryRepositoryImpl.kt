package com.itenary.shared.data.repository

import com.itenary.shared.data.mappers.toDto
import com.itenary.shared.data.mappers.toDomain
import com.itenary.shared.data.remote.ItineraryApi
import com.itenary.shared.domain.error.AppError
import com.itenary.shared.domain.model.Itinerary
import com.itenary.shared.domain.model.ItineraryRequest
import com.itenary.shared.domain.repository.ItineraryRepository
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.utils.io.errors.IOException

class ItineraryRepositoryImpl(
    private val api: ItineraryApi
) : ItineraryRepository {
    override suspend fun generate(request: ItineraryRequest): Result<Itinerary> {
        return try {
            val dto = request.toDto()
            val response = api.generate(dto)
            Result.success(response.toDomain())
        } catch (e: ClientRequestException) { // 4xx
            Result.failure(AppError.Server(code = null, httpCode = e.response.status.value, message = e.message ?: "Client error"))
        } catch (e: ServerResponseException) { // 5xx
            Result.failure(AppError.Server(code = null, httpCode = e.response.status.value, message = e.message ?: "Server error"))
        } catch (e: IOException) {
            Result.failure(AppError.Network(e))
        } catch (e: kotlinx.serialization.SerializationException) {
            Result.failure(AppError.Serialization(e))
        } catch (e: AppError) {
            Result.failure(e)
        } catch (e: Throwable) {
            Result.failure(AppError.Unknown(e))
        }
    }
}


