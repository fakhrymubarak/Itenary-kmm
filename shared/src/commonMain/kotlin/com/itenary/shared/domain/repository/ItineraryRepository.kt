package com.itenary.shared.domain.repository

import com.itenary.shared.domain.model.Itinerary
import com.itenary.shared.domain.model.ItineraryRequest

interface ItineraryRepository {
    suspend fun generate(request: ItineraryRequest): Result<Itinerary>
}


