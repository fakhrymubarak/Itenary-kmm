package com.itenary.shared.domain.repository

import com.itenary.shared.domain.model.ItineraryDomain
import com.itenary.shared.domain.model.SavedItineraryDomain
import com.itenary.shared.util.DomainResult

interface ItineraryCacheRepository {
    suspend fun cacheItinerary(key: String, itinerary: ItineraryDomain): DomainResult<Boolean>
    suspend fun cacheItineraryCopy(baseKey: String, itinerary: ItineraryDomain): DomainResult<Boolean>
    suspend fun overwriteItinerary(key: String, itinerary: ItineraryDomain): DomainResult<Boolean>
    suspend fun getSavedItinerary() : DomainResult<List<SavedItineraryDomain>>
    suspend fun getSavedItineraryByKey(key: String): DomainResult<SavedItineraryDomain>
}
