package com.itenary.shared.data.remote

import com.itenary.shared.data.database.ItineraryDao
import com.itenary.shared.data.database.ItineraryEntity
import com.itenary.shared.domain.model.ItineraryDomain
import com.itenary.shared.domain.model.SavedItineraryDomain
import com.itenary.shared.domain.repository.ItineraryCacheRepository
import com.itenary.shared.util.DomainResult
import com.itenary.shared.util.ERR_DB_INSERT_CONFLICT
import com.itenary.shared.util.ERR_ITINERARY_CACHE_ERROR
import com.itenary.shared.util.ERR_ITINERARY_EXISTS
import com.itenary.shared.util.ERR_ITINERARY_NOT_FOUND
import kotlinx.coroutines.CancellationException

class ItineraryCacheRepositoryImpl(
    private val itineraryDao: ItineraryDao,
) : ItineraryCacheRepository {
    override suspend fun cacheItinerary(key: String, itinerary: ItineraryDomain): DomainResult<Boolean> =
        try {
            val id = itineraryDao.insert(ItineraryEntity(id = key, itinerary = itinerary))
            if (id > 0) {
                DomainResult.Success(true)
            } else if (id == -1L) {
                DomainResult.Error(ERR_ITINERARY_EXISTS, ERR_DB_INSERT_CONFLICT)
            } else {
                DomainResult.Error(ERR_ITINERARY_CACHE_ERROR)
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            DomainResult.Error(message = "DB error: ${e.message}")
        }

    override suspend fun cacheItineraryCopy(
        baseKey: String,
        itinerary: ItineraryDomain,
    ): DomainResult<Boolean> =
        try {
            val existingIds = itineraryDao.getAllIds().toSet()
            val copyKey = generateCopyKey(baseKey, existingIds)
            val id = itineraryDao.insert(ItineraryEntity(id = copyKey, itinerary = itinerary))
            if (id != -1L) {
                DomainResult.Success(true)
            } else {
                DomainResult.Error("Failed to cache itinerary copy")
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            DomainResult.Error(message = "DB error: ${e.message}")
        }

    override suspend fun overwriteItinerary(
        key: String,
        itinerary: ItineraryDomain,
    ): DomainResult<Boolean> =
        try {
            itineraryDao.upsert(ItineraryEntity(id = key, itinerary = itinerary))
            DomainResult.Success(true)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            DomainResult.Error(message = "DB error: ${e.message}")
        }


    override suspend fun getSavedItinerary(): DomainResult<List<SavedItineraryDomain>> =
        try {
            val cached = itineraryDao.getAll().map { entity ->
                SavedItineraryDomain(
                    key = entity.id,
                    itinerary = entity.itinerary
                )
            }
            if (cached.isNotEmpty()) {
                DomainResult.Success(cached)
            } else {
                DomainResult.Error(ERR_ITINERARY_NOT_FOUND)
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            DomainResult.Error(message = "DB error: ${e.message}")
        }

    override suspend fun getSavedItineraryByKey(key: String): DomainResult<SavedItineraryDomain> =
        try {
            val entity = itineraryDao.getByKey(key)
            if (entity != null) {
                DomainResult.Success(
                    SavedItineraryDomain(
                        key = entity.id,
                        itinerary = entity.itinerary
                    )
                )
            } else {
                DomainResult.Error(ERR_ITINERARY_EXISTS)
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            DomainResult.Error(message = "DB error: ${e.message}")
        }

    private fun generateCopyKey(baseKey: String, existingIds: Set<String>): String {
        val base = "${baseKey}_copy"
        if (base !in existingIds) return base
        var index = 2
        while (true) {
            val candidate = "${base}_$index"
            if (candidate !in existingIds) return candidate
            index++
        }
    }
}
