package com.itenary.shared.domain.usecase

import com.itenary.shared.domain.error.AppError
import com.itenary.shared.domain.model.ItineraryRequest
import com.itenary.shared.domain.model.StreamingEvent
import com.itenary.shared.domain.repository.ItineraryStreamRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class StreamItineraryUseCase(
    private val streamApi: ItineraryStreamRepository,
) {

    operator fun invoke(request: ItineraryRequest): Flow<StreamingEvent> = flow {
        validateRequest(request)?.let {
            emit(StreamingEvent.Error(it.message ?: "Validation error"))
            return@flow
        }

        streamApi.streamItineraryGeneration(request).collect { streamingEvent ->
            println("Trace StreamItineraryUseCase event $streamingEvent")
            emit(streamingEvent)
        }
    }

    private fun validateRequest(request: ItineraryRequest): AppError.Validation? {
        if (request.place.isBlank()) return AppError.Validation("place must not be empty")
        if (request.days.isEmpty()) return AppError.Validation("days must not be empty")
        if (request.language.isBlank()) return AppError.Validation("language must not be empty")
        if (request.llm.isBlank()) return AppError.Validation("llm must not be empty")

        // Validate each day
        request.days.forEach { day ->
            if (day.day <= 0) return AppError.Validation("day number must be positive")
            if (day.startTime.isBlank()) return AppError.Validation("startTime must not be empty")
            if (day.endTime.isBlank()) return AppError.Validation("endTime must not be empty")
            if (day.pace < 1) return AppError.Validation("pace must not be positive")
        }

        return null
    }
}
