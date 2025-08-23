package com.itenary.shared.domain.repository

import com.itenary.shared.domain.model.ItineraryRequest
import com.itenary.shared.domain.model.StreamingEvent
import kotlinx.coroutines.flow.Flow

interface ItineraryStreamRepository {
    suspend fun streamItineraryGeneration(request: ItineraryRequest): Flow<StreamingEvent>
}