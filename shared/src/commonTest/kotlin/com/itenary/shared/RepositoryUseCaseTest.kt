package com.itenary.shared

import com.itenary.shared.data.dto.ItineraryRequestDto
import com.itenary.shared.data.dto.ItineraryResponseDto
import com.itenary.shared.data.remote.ItineraryApi
import com.itenary.shared.data.repository.ItineraryRepositoryImpl
import com.itenary.shared.domain.error.AppError
import com.itenary.shared.domain.model.*
import com.itenary.shared.domain.repository.ItineraryRepository
import com.itenary.shared.domain.usecase.GenerateItineraryUseCase
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

private class FakeApiSuccess : ItineraryApi {
    override suspend fun generate(dto: ItineraryRequestDto): ItineraryResponseDto = ItineraryResponseDto(
        result = emptyList()
    )
}

private class FakeApiError : ItineraryApi {
    override suspend fun generate(dto: ItineraryRequestDto): ItineraryResponseDto {
        throw AppError.Server(code = "E", httpCode = 500, message = "err")
    }
}

class RepositoryUseCaseTest {
    @Test
    fun repository_success() = runTest {
        val repo: ItineraryRepository = ItineraryRepositoryImpl(FakeApiSuccess())
        val res = repo.generate(
            ItineraryRequest("c","p","ID","2025-01-01","2025-01-02","en")
        )
        assertTrue(res.isSuccess)
    }

    @Test
    fun repository_error() = runTest {
        val repo: ItineraryRepository = ItineraryRepositoryImpl(FakeApiError())
        val res = repo.generate(
            ItineraryRequest("c","p","ID","2025-01-01","2025-01-02","en")
        )
        assertTrue(res.isFailure)
    }

    @Test
    fun usecase_validation_errors() = runTest {
        val usecase = GenerateItineraryUseCase(ItineraryRepositoryImpl(FakeApiSuccess()))
        val bad = GenerateItineraryUseCase.Params("","p","ID","2025-01-02","2025-01-01","en")
        val res = usecase(bad)
        assertTrue(res.isFailure)
        assertTrue(res.exceptionOrNull() is AppError.Validation)
    }

    @Test
    fun usecase_success() = runTest {
        val usecase = GenerateItineraryUseCase(ItineraryRepositoryImpl(FakeApiSuccess()))
        val ok = GenerateItineraryUseCase.Params("c","p","ID","2025-01-01","2025-01-02","en")
        val res = usecase(ok)
        assertTrue(res.isSuccess)
        assertEquals(0, res.getOrThrow().result.size)
    }
}


