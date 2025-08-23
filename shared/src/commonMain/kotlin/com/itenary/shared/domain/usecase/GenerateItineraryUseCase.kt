package com.itenary.shared.domain.usecase

import com.itenary.shared.domain.error.AppError
import com.itenary.shared.domain.model.Itinerary
import com.itenary.shared.domain.model.ItineraryRequest
import com.itenary.shared.domain.repository.ItineraryRepository
import com.itenary.shared.util.DateUtils

class GenerateItineraryUseCase(
    private val repository: ItineraryRepository
) {
    data class Params(
        val city: String,
        val province: String,
        val countryCode: String,
        val startDate: String,
        val endDate: String,
        val language: String
    )

    suspend operator fun invoke(params: Params): Result<Itinerary> {
        validate(params)?.let { return Result.failure(it) }

        val request = ItineraryRequest(
            city = params.city.trim(),
            province = params.province.trim(),
            countryCode = params.countryCode.trim(),
            startDate = params.startDate,
            endDate = params.endDate,
            language = params.language.trim()
        )
        return repository.generate(request)
    }

    private fun validate(params: Params): AppError.Validation? {
        if (params.city.isBlank()) return AppError.Validation("city must not be empty")
        if (params.province.isBlank()) return AppError.Validation("province must not be empty")
        if (params.countryCode.isBlank()) return AppError.Validation("countryCode must not be empty")
        if (params.language.isBlank()) return AppError.Validation("language must not be empty")
        if (!DateUtils.isValidDateIso(params.startDate)) return AppError.Validation("startDate must be YYYY-MM-DD")
        if (!DateUtils.isValidDateIso(params.endDate)) return AppError.Validation("endDate must be YYYY-MM-DD")
        if (!DateUtils.isStartBeforeOrEqualEnd(params.startDate, params.endDate)) {
            return AppError.Validation("startDate must be before or equal to endDate")
        }
        return null
    }
}


