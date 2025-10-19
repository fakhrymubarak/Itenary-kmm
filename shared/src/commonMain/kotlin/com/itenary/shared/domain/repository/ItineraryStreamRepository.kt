package com.itenary.shared.domain.repository

import com.itenary.shared.domain.model.ItineraryDomain
import com.itenary.shared.domain.model.ItineraryRequest
import com.itenary.shared.domain.model.StreamingEvent
import com.itenary.shared.util.DomainResult
import kotlinx.coroutines.flow.Flow

interface ItineraryStreamRepository {
    suspend fun streamItineraryGeneration(request: ItineraryRequest): Flow<StreamingEvent>
    suspend fun cacheItinerary(key: String, itinerary: ItineraryDomain): DomainResult<Boolean>
}
